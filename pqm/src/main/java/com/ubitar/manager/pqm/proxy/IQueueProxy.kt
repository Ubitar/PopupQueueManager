package com.ubitar.manager.pqm.proxy

import com.ubitar.manager.pqm.group.IGroup
import com.ubitar.manager.pqm.task.base.ITask

/**
 * 弹窗行为代理
 */
interface IQueueProxy {

    fun onDismiss()

    fun getTask():ITask

    fun getGroup(): IGroup

}