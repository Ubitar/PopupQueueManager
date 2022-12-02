package com.ubitar.manager.pqm.delegate

import android.os.Handler
import android.os.Looper
import androidx.annotation.MainThread
import com.ubitar.manager.pqm.PopupQueueManager
import com.ubitar.manager.pqm.group.QueueGroup
import com.ubitar.manager.pqm.popup.IQueuePopup
import com.ubitar.manager.pqm.proxy.PopupQueueProxy
import com.ubitar.manager.pqm.task.base.IAsyncTask
import com.ubitar.manager.pqm.task.base.ISyncTask
import com.ubitar.manager.pqm.task.base.ITask
import com.ubitar.manager.pqm.task.base.ITaskRetry
import java.util.*

class QueueDelegate(
    private val mGroup: QueueGroup,
    private val mQueue: Queue<ITask>,
) : IDelegate {

    /** 队列是否正在运行 */
    private var mIsRunning = false

    /** 当前正在执行的任务 */
    private var mCurrentTask: ITask? = null

    /** 延时计时器 */
    private var mDelayTimer: Timer? = null

    /** 主线程回调Handler */
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

    /**
     *  清空队列
     *  @param withCurrent 是否包含当前正在运行的弹窗任务（注：为true时也不会自动关闭当前弹窗，只能清除正在运行的任务）
     */
    override fun clear(withCurrent: Boolean) {
        mGroup.mOnBeforeClearListeners.forEach { it.second.invoke(mGroup) }
        if (withCurrent) clearCurrentTask()
        mQueue.clear()
        mGroup.mOnAfterClearListeners.forEach { it.second.invoke(mGroup) }
    }

    /** 预备开始下一个任务 */
    private fun postToNextTask() {
        val onMainRun = fun() {
            if (!mIsRunning) return
            if (mCurrentTask != null) return
            if (getCurrentSize() <= 0) return
            onDoingNextTask()
        }
        if (Looper.myLooper() == Looper.getMainLooper()) onMainRun()
        else mHandler.post { onMainRun() }
    }

    /**
     *  正在进行下一个任务
     * @param isRetry 是否是重试任务
     * */
    @MainThread
    private fun onDoingNextTask(isRetry: Boolean = false) {
        val isStopAfterFinish =
            PopupQueueManager.getStopAfterFinish() || mGroup.getStopAfterFinish()
        if (mQueue.isEmpty()) {
            if (isStopAfterFinish) mIsRunning = false
            mGroup.mOnGroupFinishListeners.forEach { it.second.invoke(mGroup) }
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
        currentTask.onTaskStart()

        onBeforeNextTask(currentTask) {

            val isIntercept = onDispatchInterceptTask(currentTask)

            if (isIntercept) {
                onInterceptTask()
                return@onBeforeNextTask
            }

            onRealCurrentTask(currentTask, isRetry) {

                onAfterCurrentTask(currentTask) {

                    onFinishCurrentTask()

                    onDoingNextTask()

                }
            }
        }
    }

    /** 分发是否终止当前分组的监听 */
    private fun onDispatchInterruptGroup(): Boolean {
        return mGroup.mOnInterruptGroupListeners.any { it.second.invoke(mGroup) }
    }

    /** 分发是否拦截当前任务的监听 */
    private fun onDispatchInterceptTask(task: ITask): Boolean {
        return mGroup.mOnInterceptTaskListeners.any { it.second.invoke(mGroup, task) }
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

    /**
     * 真正执行当前任务
     * @param isRetry 是否是重试任务
     * */
    private fun onRealCurrentTask(currentTask: ITask, isRetry: Boolean, onComplete: () -> Unit) {
        val onCreatedPopup = fun(task: ITask, popup: IQueuePopup) {
            popup.onCatchQueueProxy(PopupQueueProxy(task, mGroup) {
                onComplete.invoke()
            })
            mGroup.mOnNextTaskListeners.forEach {
                it.second.invoke(mGroup, task, popup)
            }
            task.show(popup)
            return
        }
        val onFailPopup = fun(task: ITask) {
            if (task is ITaskRetry) {
                if (task.getRetryCount() > task.getCurrentRetryCount()) {
                    resetCurrentTask()
                    onDoingNextTask(true)
                } else onComplete.invoke()
            }
        }
        val onCancelPopup = fun(task: ITask) {
            onComplete.invoke()
        }

        if (isRetry) {
            if (currentTask is ITaskRetry) {
                currentTask.plushOneRetryCount()
            }
        }

        when (currentTask) {
            is IAsyncTask -> {
                currentTask.onCreatePopup(object : IAsyncTask.IExecuteCallback {
                    override fun onCreatedSuccess(popup: IQueuePopup) {
                        onCreatedPopup.invoke(currentTask, popup)
                    }

                    override fun onCreateFail() {
                        onFailPopup.invoke(currentTask)
                    }

                    override fun onCreateCancel() {
                        onCancelPopup.invoke(currentTask)
                    }

                })
            }
            is ISyncTask -> {
                val popup = currentTask.onCreatePopup()
                if (popup == null) onCancelPopup.invoke(currentTask)
                else onCreatedPopup.invoke(currentTask, popup)
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
        clearCurrentTask()
    }

    /** 重试前重置当前任务 */
    private fun resetCurrentTask() {
        mCurrentTask = null
    }

    /** 清除当前任务 */
    private fun clearCurrentTask() {
        mQueue.poll()
        mCurrentTask?.onTaskFinish()
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
            if (o1.isRunning() || o2.isRunning()) return@Comparator 0

            val p1 = o1?.getPriority() ?: 0
            val p2 = o2?.getPriority() ?: 0
            if(p1==p2) 1 else p1-p2
        }
    }

}