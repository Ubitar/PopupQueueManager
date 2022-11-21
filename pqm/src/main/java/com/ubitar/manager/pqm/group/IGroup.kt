package com.ubitar.manager.pqm.group

import androidx.lifecycle.LifecycleOwner
import com.ubitar.manager.pqm.popup.IQueuePopup
import com.ubitar.manager.pqm.task.base.ITask

/**
 * 队列分组
 */
interface IGroup {

    fun addOnGroupFinishListener(listener: (group: IGroup) -> Unit)

    fun addOnInterruptGroupListener(listener: (group: IGroup) -> Boolean)

    fun addOnInterceptTaskListener(listener: (group: IGroup, task: ITask) -> Boolean)

    fun addOnNextTaskListener(listener: (group: IGroup, task: ITask, popup: IQueuePopup) -> Unit)

    fun removeOnGroupFinishListener(listener: (group: IGroup) -> Unit)

    fun removeOnInterruptGroupListener(listener: (group: IGroup) -> Boolean)

    fun removeOnInterceptTaskListener(listener: (group: IGroup, task: ITask) -> Boolean)

    fun removeOnNextTaskListener(listener: (group: IGroup, task: ITask, popup: IQueuePopup) -> Unit)

    fun observeOnGroupFinishListener(lifecycleOwner: LifecycleOwner, listener: (group: IGroup) -> Unit)

    fun observeOnInterruptGroupListener(lifecycleOwner: LifecycleOwner, listener: (group: IGroup) -> Boolean)

    fun observeOnInterceptTaskListener(lifecycleOwner: LifecycleOwner, listener: (group: IGroup, task: ITask) -> Boolean)

    fun observeOnNextTaskListener(lifecycleOwner: LifecycleOwner, listener: (group: IGroup, task: ITask, popup: IQueuePopup) -> Unit)

    fun removeObserveOnGroupFinishListener(lifecycleOwner: LifecycleOwner)

    fun removeObserveOnInterruptGroupListener(lifecycleOwner: LifecycleOwner)

    fun removeObserveOnInterceptTaskListener(lifecycleOwner: LifecycleOwner)

    fun removeObserveOnNextTaskListener(lifecycleOwner: LifecycleOwner)

    fun push(task: ITask)

    fun pushAndStart(task: ITask)

    fun setBeforeTaskDelay(delay: Long)

    fun setAfterTaskDelay(delay: Long)

    fun getBeforeTaskDelay(): Long

    fun getAfterTaskDelay(): Long

    fun setStopAfterFinish(isStop: Boolean)

    fun getStopAfterFinish(): Boolean

    fun start()

    fun stop()

    fun getTasks(): List<ITask>

    fun getCurrentSize(): Int

    fun isRunning(): Boolean

    fun clear()

}