package com.ubitar.manager.pqm.task.base

import com.ubitar.manager.pqm.popup.IQueuePopup

/**
 * 同步的弹窗任务
 */
interface ISyncTask : ITask {

    /** 是否是异步任务 */
    override fun isAsync(): Boolean = false

    /** 创建弹窗 */
    fun onCreatePopup(): IQueuePopup?

}