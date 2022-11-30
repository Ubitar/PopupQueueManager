package com.ubitar.manager.pqm.proxy

import com.ubitar.manager.pqm.group.IGroup
import com.ubitar.manager.pqm.task.base.ITask

open class PopupQueueProxy(
    private val mTask:ITask,
    private val mGroup:IGroup,
    private val mOnDismiss: () -> Unit
) : IQueueProxy {
    override fun onDismiss() {
        mOnDismiss.invoke()
    }

    override fun getTask(): ITask {
        return mTask
    }

    override fun getGroup(): IGroup {
        return mGroup
    }
}