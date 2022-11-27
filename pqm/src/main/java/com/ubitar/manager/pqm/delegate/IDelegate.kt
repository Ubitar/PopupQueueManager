package com.ubitar.manager.pqm.delegate

import com.ubitar.manager.pqm.task.base.ITask

/**
 * 队列运行逻辑-委托类
 */
interface IDelegate {

    /** 入栈新的任务 */
    fun push(task: ITask)

    /** 入栈新的任务且运行队列 */
    fun pushAndStart(task: ITask)

    /** 开始运行队列 */
    fun start()

    /** 停止运行队列 */
    fun stop()

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