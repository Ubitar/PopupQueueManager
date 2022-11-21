package com.ubitar.manager.pqm.task.base

import com.ubitar.manager.pqm.popup.IQueuePopup

/**
 * 异步的弹窗队列
 */
interface IAsyncTask: ITask {

    override fun isAsync(): Boolean = true

    fun onCreatePopup(callback: IExecuteCallback)

    interface IExecuteCallback {

        fun onCreatedSuccess(popup: IQueuePopup)

        fun onCreateFail()

        fun onCreateCancel()

    }

}