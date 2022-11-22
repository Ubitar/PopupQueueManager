package com.ubitar.manager.pqm.task.base

import com.ubitar.manager.pqm.popup.IQueuePopup

/**
 * 异步的弹窗队列
 */
interface IAsyncTask: ITask {

    /** 是否是异步任务 */
    override fun isAsync(): Boolean = true

    /** 创建弹窗 */
    fun onCreatePopup(callback: IExecuteCallback)

    /** 弹窗创建结果的回调 */
    interface IExecuteCallback {

        fun onCreatedSuccess(popup: IQueuePopup)

        fun onCreateFail()

        fun onCreateCancel()

    }

}