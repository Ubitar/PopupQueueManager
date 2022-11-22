package com.ubitar.manager.pqm.delegate

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LifecycleOwner
import com.ubitar.manager.pqm.PopupQueueManager
import com.ubitar.manager.pqm.group.IGroup
import com.ubitar.manager.pqm.popup.IQueuePopup
import com.ubitar.manager.pqm.proxy.PopupQueueProxy
import com.ubitar.manager.pqm.task.base.IAsyncTask
import com.ubitar.manager.pqm.task.base.ISyncTask
import com.ubitar.manager.pqm.task.base.ITask
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

class QueueDelegate(
    private val mGroup: IGroup,
    private val mQueue: Queue<ITask>,
    private val mOnGroupFinishListeners: CopyOnWriteArrayList<Pair<LifecycleOwner?, (group: IGroup) -> Unit>>,
    private val mOnInterruptGroupListeners: CopyOnWriteArrayList<Pair<LifecycleOwner?, (group: IGroup) -> Boolean>>,
    private val mOnInterceptTaskListeners: CopyOnWriteArrayList<Pair<LifecycleOwner?, (group: IGroup, task: ITask) -> Boolean>>,
    private val mOnNextTaskListeners: CopyOnWriteArrayList<Pair<LifecycleOwner?, (group: IGroup, task: ITask, popup: IQueuePopup) -> Unit>>
) : IDelegate {

    /**
     * 队列是否正在运行
     */
    private var mIsRunning = false

    /**
     * 当前正在执行的任务
     */
    private var mCurrentTask: ITask? = null

    /**
     * 延时计时器
     */
    private var mDelayTimer: Timer? = null

    /**
     * 主线程回调Handler
     */
    private val mHandler = Handler(Looper.getMainLooper())

    override fun push(task: ITask) {
        mQueue.add(task)
        if (mQueue is List<*>)
            Collections.sort(mQueue as List<ITask>, COMPARATOR)
        if (mIsRunning) postToNextTask()
    }

    override fun pushAndStart(task: ITask) {
        mQueue.add(task)
        if (mQueue is List<*>)
            Collections.sort(mQueue as List<ITask>, COMPARATOR)
        if (mIsRunning) postToNextTask()
        else start()
    }

    override fun start() {
        mIsRunning = true
        postToNextTask()
    }

    override fun stop() {
        if (!mIsRunning) return
        mIsRunning = false
    }

    override fun getCurrentSize(): Int {
        return mQueue.size
    }

    override fun isRunning(): Boolean {
        return mIsRunning
    }

    override fun clear() {
        if (getCurrentSize() <= 0) return
        if (mCurrentTask == mQueue.peek()) {
            mQueue.filter { it != mCurrentTask }
                .also { mQueue.removeAll(it) }
        } else {
            mQueue.clear()
        }
    }

    private fun postToNextTask() {
        if (mCurrentTask != null) return
        if (mQueue.isEmpty()) return
        onDoingNextTask()
    }

    private fun onDoingNextTask() {
        val isStopAfterFinish = PopupQueueManager.getStopAfterFinish() || mGroup.getStopAfterFinish()

        if (mQueue.isEmpty()) {
            if (isStopAfterFinish) mIsRunning = false
            mOnGroupFinishListeners.forEach { it.second.invoke(mGroup) }
            return
        }

        val isInterruptGroup = onDispatchInterruptGroup()
        if (isInterruptGroup) {
            onInterruptGroup()
            return
        }

        val nextTask = mQueue.peek() ?: return
        mCurrentTask = nextTask

        onBeforeNextTask(nextTask) {

            val isIntercept = onDispatchInterceptTask(nextTask)

            if (isIntercept) {
                onInterceptTask()
                return@onBeforeNextTask
            }

            onRealNextTask(nextTask) {

                onAfterNextTask(nextTask) {

                    onFinishThisTask()

                    onDoingNextTask()

                }
            }
        }
    }

    private fun onDispatchInterruptGroup(): Boolean {
        return mOnInterruptGroupListeners.any { it.second.invoke(mGroup) }
            .also {
                if (it) stop()
            }
    }

    private fun onDispatchInterceptTask(task: ITask): Boolean {
        return mOnInterceptTaskListeners.any { it.second.invoke(mGroup, task) }
    }

    private fun onInterruptGroup() {
        //nothing
    }

    private fun onInterceptTask() {
        clearCurrentTask()
        onDoingNextTask()
    }

    private fun onBeforeNextTask(task: ITask, onComplete: () -> Unit) {
        val beforeTaskDelay = Math.max(
            mGroup.getBeforeTaskDelay(),
            task.getBeforeDelay()
        )

        if (beforeTaskDelay > 0) {
            mDelayTimer?.cancel()
            mDelayTimer = delayWith(beforeTaskDelay) {
                val isInterruptGroup = onDispatchInterruptGroup()
                if (isInterruptGroup) {
                    onInterruptGroup()
                } else {
                    onComplete.invoke()
                }
            }
        } else {
            onComplete.invoke()
        }
    }

    private fun onRealNextTask(currentTask: ITask, onComplete: () -> Unit) {
        val onCreatedPopup = fun(task: ITask, popup: IQueuePopup?) {
            if (popup == null) {
                clearCurrentTask()
                onDoingNextTask()
                return
            } else {
                popup.onCatchQueueProxy(PopupQueueProxy {
                    onComplete.invoke()
                })
                mOnNextTaskListeners.forEach {
                    it.second.invoke(mGroup, task, popup)
                }
                task.show(popup)
                return
            }

        }

        when (currentTask) {
            is IAsyncTask -> {
                currentTask.onCreatePopup(object : IAsyncTask.IExecuteCallback {
                    override fun onCreatedSuccess(popup: IQueuePopup) {
                        onCreatedPopup.invoke(currentTask, popup)
                    }

                    override fun onCreateFail() {
                        onCreatedPopup.invoke(currentTask, null)
                    }

                    override fun onCreateCancel() {
                        onCreatedPopup.invoke(currentTask, null)
                    }

                })
            }
            is ISyncTask -> {
                onCreatedPopup.invoke(currentTask, currentTask.onCreatePopup())
            }
        }
    }

    private fun onAfterNextTask(task: ITask, onComplete: () -> Unit) {
        val afterTaskDelay = Math.max(
            mGroup.getAfterTaskDelay(),
            task.getAfterDelay()
        )

        if (afterTaskDelay > 0) {
            mDelayTimer?.cancel()
            mDelayTimer = delayWith(afterTaskDelay) {
                onComplete.invoke()
            }
        } else {
            onComplete.invoke()
        }
    }

    private fun onFinishThisTask() {
        clearCurrentTask()
    }

    private fun clearCurrentTask() {
        mQueue.poll()
        mCurrentTask = null
    }

    private fun delayWith(delay: Long, onComplete: () -> Unit): Timer {
        return Timer().also {
            it.schedule(object : TimerTask() {
                override fun run() {
                    mHandler.post {
                        onComplete.invoke()
                    }
                }

            }, delay)
        }
    }

    companion object {
        val COMPARATOR = Comparator<ITask> { o1, o2 ->
            val p1 = o1?.getPriority() ?: 0
            val p2 = o2?.getPriority() ?: 0
            p1 - p2
        }
    }

}