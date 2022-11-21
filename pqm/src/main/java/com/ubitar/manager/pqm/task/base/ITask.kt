package com.ubitar.manager.pqm.task.base

import com.ubitar.manager.pqm.popup.IQueuePopup

/**
 * 弹窗任务
 */
interface ITask {

    fun isAsync(): Boolean

    fun getBeforeDelay(): Long = -1

    fun getAfterDelay(): Long = -1

    fun show( popup: IQueuePopup)

    fun getPriority(): Int = DEFAULT_PRIORITY

    fun getTag(): String? = null

    companion object {
        const val DEFAULT_PRIORITY = 1000
    }

}