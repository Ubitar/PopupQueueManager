package com.ubitar.manager.pqm.group

import androidx.lifecycle.LifecycleOwner
import com.ubitar.manager.pqm.popup.IQueuePopup
import com.ubitar.manager.pqm.task.base.ITask

interface IGroupListeners {

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

}