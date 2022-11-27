package com.ubitar.manager.pqm.group

import androidx.lifecycle.LifecycleOwner
import com.ubitar.manager.pqm.popup.IQueuePopup
import com.ubitar.manager.pqm.task.base.ITask

/**
 * 队列分组
 */
interface IGroup {

    /** 添加分组播放结束的监听 */
    fun addOnGroupFinishListener(listener: (group: IGroup) -> Unit)

    /** 添加终止分组的监听 */
    fun addOnInterruptGroupListener(listener: (group: IGroup) -> Boolean)

    /** 添加拦截本次任务的监听 */
    fun addOnInterceptTaskListener(listener: (group: IGroup, task: ITask) -> Boolean)

    /** 添加开始下一个任务的监听 */
    fun addOnNextTaskListener(listener: (group: IGroup, task: ITask, popup: IQueuePopup) -> Unit)

    /** 添加清除队列前的监听 */
    fun addOnBeforeClearListener(listener: (group: IGroup) -> Unit)

    /** 添加清除队列后的监听 */
    fun addOnAfterClearListener(listener: (group: IGroup) -> Unit)

    /** 移除分组播放结束的监听 */
    fun removeOnGroupFinishListener(listener: (group: IGroup) -> Unit)

    /** 移除终止分组的监听 */
    fun removeOnInterruptGroupListener(listener: (group: IGroup) -> Boolean)

    /** 移除拦截本次任务的监听 */
    fun removeOnInterceptTaskListener(listener: (group: IGroup, task: ITask) -> Boolean)

    /** 移除开始下一个任务的监听 */
    fun removeOnNextTaskListener(listener: (group: IGroup, task: ITask, popup: IQueuePopup) -> Unit)

    /** 移除清除队列前的监听 */
    fun removeOnBeforeClearListener(listener: (group: IGroup) -> Unit)

    /** 移除清除队列后的监听 */
    fun removeOnAfterClearListener(listener: (group: IGroup) -> Unit)

    /** 添加分组播放结束的监听 */
    fun observeOnGroupFinishListener(lifecycleOwner: LifecycleOwner, listener: (group: IGroup) -> Unit)

    /** 添加终止分组的监听 */
    fun observeOnInterruptGroupListener(lifecycleOwner: LifecycleOwner, listener: (group: IGroup) -> Boolean)

    /** 添加拦截本次任务的监听 */
    fun observeOnInterceptTaskListener(lifecycleOwner: LifecycleOwner, listener: (group: IGroup, task: ITask) -> Boolean)

    /** 添加开始下一个任务的监听 */
    fun observeOnNextTaskListener(lifecycleOwner: LifecycleOwner, listener: (group: IGroup, task: ITask, popup: IQueuePopup) -> Unit)

    /** 添加清除队列前的监听 */
    fun observeOnBeforeClearListener(lifecycleOwner: LifecycleOwner, listener: (group: IGroup) -> Unit)

    /** 添加清除队列后的监听 */
    fun observeOnAfterClearListener(lifecycleOwner: LifecycleOwner, listener: (group: IGroup) -> Unit)

    /** 移除分组播放结束的监听 */
    fun removeObserveOnGroupFinishListener(lifecycleOwner: LifecycleOwner)

    /** 移除终止分组的监听 */
    fun removeObserveOnInterruptGroupListener(lifecycleOwner: LifecycleOwner)

    /** 移除拦截本次任务的监听 */
    fun removeObserveOnInterceptTaskListener(lifecycleOwner: LifecycleOwner)

    /** 移除开始下一个任务的监听 */
    fun removeObserveOnNextTaskListener(lifecycleOwner: LifecycleOwner)

    /** 移除清除队列前的监听 */
    fun removeObserveOnBeforeClearListener(lifecycleOwner: LifecycleOwner)

    /** 移除清除队列后的监听 */
    fun removeObserveOnAfterClearListener(lifecycleOwner: LifecycleOwner)

    /** 入栈新的任务 */
    fun push(task: ITask)

    /** 入栈新的任务且运行队列 */
    fun pushAndStart(task: ITask)

    /** 设置任务开始前的延迟 */
    fun setBeforeTaskDelay(delay: Long)

    /** 设置任务结束后的延迟 */
    fun setAfterTaskDelay(delay: Long)

    /** 获取任务开始前的延迟 */
    fun getBeforeTaskDelay(): Long

    /** 获取任务结束后的延迟 */
    fun getAfterTaskDelay(): Long

    /** 设置分组播放结束后就停止 */
    fun setStopAfterFinish(isStop: Boolean)

    /** 获取分组播放结束后就停止 */
    fun getStopAfterFinish(): Boolean

    /** 开始运行队列 */
    fun start()

    /** 停止运行队列 */
    fun stop()

    /** 获取任务列表 */
    fun getTasks(): List<ITask>

    /** 当前队列大小 */
    fun getCurrentSize(): Int

    /** 队列是否在运行 */
    fun isRunning(): Boolean

    /**
     *  清空队列
     *  @param withCurrent 是否包含当前正在运行的弹窗任务（注：为true时也不会自动关闭当前弹窗，只能清除正在运行的任务）
     */
    fun clear(withCurrent: Boolean = true)

}