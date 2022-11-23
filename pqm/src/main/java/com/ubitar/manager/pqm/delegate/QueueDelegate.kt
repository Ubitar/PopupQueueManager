package com.ubitar.manager.pqm.delegate

import android.os.Handler
import android.os.Looper
import androidx.annotation.MainThread
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

    /** 入栈新的任务 */
    override fun push(task: ITask) {
        mQueue.add(task)
        if (mQueue is List<*>)
            Collections.sort(mQueue as List<ITask>, COMPARATOR)
        if (mIsRunning) postToNextTask()
    }

    /** 入栈新的任务且运行队列 */
    override fun pushAndStart(task: ITask) {
        mQueue.add(task)
        if (mQueue is List<*>)
            Collections.sort(mQueue as List<ITask>, COMPARATOR)
        if (mIsRunning) postToNextTask()
        else start()
    }

    /** 开始运行队列 */
    override fun start() {
        if (mIsRunning) return
        mIsRunning = true
        postToNextTask()
    }

    /** 停止运行队列 */
    override fun stop() {
        if (!mIsRunning) return
        mIsRunning = false
    }

    /** 当前队列大小 */
    override fun getCurrentSize(): Int {
        return mQueue.size
    }

    /** 队列是否在运行 */
    override fun isRunning(): Boolean {
        return mIsRunning
    }

    /** 清空队列 */
    override fun clear() {
        mQueue.clear()
    }

    /** 预备开始下一个任务 */
    private fun postToNextTask() {
        mHandler.post {
            if (!mIsRunning) return@post
            if (mCurrentTask != null) return@post
            if (getCurrentSize() <= 0) return@post
            onDoingNextTask()
        }
    }

    /** 正在进行下一个任务 */
    @MainThread
    private fun onDoingNextTask() {
        val isStopAfterFinish = PopupQueueManager.getStopAfterFinish() || mGroup.getStopAfterFinish()
        if (mQueue.isEmpty()) {
            if (isStopAfterFinish) mIsRunning = false
            mOnGroupFinishListeners.forEach { it.second.invoke(mGroup) }
            return
        }

        if (!mIsRunning) return

        val isInterruptGroup = onDispatchInterruptGroup()
        if (isInterruptGroup) {
            onInterruptGroup()
            return
        }

        val currentTask = mQueue.peek() ?: return
        mCurrentTask = currentTask

        onBeforeNextTask(currentTask) {

            val isIntercept = onDispatchInterceptTask(currentTask)

            if (isIntercept) {
                onInterceptTask()
                return@onBeforeNextTask
            }

            onRealCurrentTask(currentTask) {

                onAfterCurrentTask(currentTask) {

                    onFinishCurrentTask()

                    onDoingNextTask()

                }
            }
        }
    }

    /** 分发是否终止当前分组的监听 */
    private fun onDispatchInterruptGroup(): Boolean {
        return mOnInterruptGroupListeners.any { it.second.invoke(mGroup) }
    }

    /** 分发是否拦截当前任务的监听 */
    private fun onDispatchInterceptTask(task: ITask): Boolean {
        return mOnInterceptTaskListeners.any { it.second.invoke(mGroup, task) }
    }

    /** 终止当前组 */
    private fun onInterruptGroup() {
        stop()
    }

    /** 拦截当前任务 */
    private fun onInterceptTask() {
        clearCurrentTask()
        onDoingNextTask()
    }

    /** 任务开始前 */
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

    /** 真正执行当前任务 */
    private fun onRealCurrentTask(currentTask: ITask, onComplete: () -> Unit) {
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

    /** 当前任务执行后 */
    private fun onAfterCurrentTask(task: ITask, onComplete: () -> Unit) {
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

    /** 完成该任务后 */
    private fun onFinishCurrentTask() {
        mQueue.poll()
        clearCurrentTask()
    }

    /** 清除当前任务 */
    private fun clearCurrentTask() {
        mCurrentTask = null
    }

    /** 延迟器 */
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