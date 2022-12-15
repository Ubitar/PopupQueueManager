package com.ubitar.manager.pqm.task.base

import com.ubitar.manager.pqm.popup.IQueuePopup

/**
 * 弹窗任务
 */
interface ITask {

    /** 是否是异步任务 */
    fun isAsync(): Boolean

    /** 任务开始前的延迟 */
    fun getBeforeDelay(): Long = -1

    /** 任务结束后的延迟 */
    fun getAfterDelay(): Long = -1

    /** 显示弹窗 */
    fun show(popup: IQueuePopup)

    /** 任务优先级 */
    fun getPriority(): Int = DEFAULT_PRIORITY

    /** 任务开始 */
    fun onTaskStart()

    /** 任务重新开始 */
    fun onTaskRestart()

    /** 任务取消 */
    fun onTaskCancel()

    /** 任务完成 */
    fun onTaskFinish()

    /** 任务结束 */
    fun onTaskComplete()

    /** 是否正在执行 */
    fun isRunning(): Boolean

    fun getTag(): String? = null

    companion object {
        //默认的优先级
        const val DEFAULT_PRIORITY = 1000
    }

}