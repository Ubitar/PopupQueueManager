package com.ubitar.manager.pqm.task.base

import com.ubitar.manager.pqm.popup.IQueuePopup

/**
 * 同步的弹窗任务
 */
interface ISyncTask : ITask {

    override fun isAsync(): Boolean = false

    fun onCreatePopup(): IQueuePopup?

}