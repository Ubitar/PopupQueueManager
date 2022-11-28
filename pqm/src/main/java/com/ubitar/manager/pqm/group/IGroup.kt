package com.ubitar.manager.pqm.group

import com.ubitar.manager.pqm.task.base.ITask

/**
 * 队列分组
 */
interface IGroup {

    /** 返回分组标识 */
    fun groups():Groups

    /** 入栈新的任务 */
    fun push(task: ITask)

    /** 入栈新的任务且运行队列 */
    fun pushAndStart(task: ITask)

    /** 设置任务开始前的延迟 */
    fun setBeforeTaskDelay(delay: Long)

    /** 设置任务结束后的延迟 */
    fun setAfterTaskDelay(delay: Long)

    /** 获取任务开始前的延迟 */
    fun getBeforeTaskDelay(): Long

    /** 获取任务结束后的延迟 */
    fun getAfterTaskDelay(): Long

    /** 设置分组播放结束后就停止 */
    fun setStopAfterFinish(isStop: Boolean)

    /** 获取分组播放结束后就停止 */
    fun getStopAfterFinish(): Boolean

    /** 开始运行队列 */
    fun start()

    /** 停止运行队列 */
    fun stop()

    /** 获取任务列表 */
    fun getTasks(): List<ITask>

    /** 当前队列大小 */
    fun getCurrentSize(): Int

    /** 队列是否在运行 */
    fun isRunning(): Boolean

    /**
     *  清空队列
     *  @param withCurrent 是否包含当前正在运行的弹窗任务（注：为true时也不会自动关闭当前弹窗，只能清除正在运行的任务）
     */
    fun clear(withCurrent: Boolean = true)

}