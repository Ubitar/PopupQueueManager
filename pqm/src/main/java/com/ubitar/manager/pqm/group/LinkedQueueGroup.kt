package com.ubitar.manager.pqm.group

import com.ubitar.manager.pqm.delegate.IDelegate
import com.ubitar.manager.pqm.delegate.QueueDelegate
import com.ubitar.manager.pqm.task.base.ITask
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

class LinkedQueueGroup : QueueGroup() {

    /** 任务开始前的延迟 */
    private var mBeforeDelay = -1L

    /** 任务结束后的延迟 */
    private var mAfterDelay = -1L

    /** 是否队列播放结束后停止队列 */
    private var mIsStopAfterFinish = false

    /** 任务队列 */
    private val mQueue: Queue<ITask> = ConcurrentLinkedQueue()

    /** 队列托管 */
    private val mDelegate: IDelegate by lazy {
        QueueDelegate(mGroup = this, mQueue = mQueue)
    }

    /** 入栈新的任务 */
    override fun push(task: ITask) {
        mDelegate.push(task)
    }

    /** 入栈新的任务且运行队列 */
    override fun pushAndStart(task: ITask) {
        mDelegate.pushAndStart(task)
    }

    /** 设置任务开始前的延迟 */
    override fun setBeforeTaskDelay(delay: Long) {
        this.mBeforeDelay = delay
    }

    /** 设置任务结束后的延迟 */
    override fun setAfterTaskDelay(delay: Long) {
        this.mAfterDelay = delay
    }

    /** 获取任务开始前的延迟 */
    override fun getBeforeTaskDelay(): Long {
        return mBeforeDelay
    }

    /** 获取任务结束后的延迟 */
    override fun getAfterTaskDelay(): Long {
        return mAfterDelay
    }

    /** 设置分组播放结束后就停止 */
    override fun setStopAfterFinish(isStop: Boolean) {
        mIsStopAfterFinish = isStop
    }

    /** 获取分组播放结束后就停止 */
    override fun getStopAfterFinish(): Boolean {
        return mIsStopAfterFinish
    }

    /** 开始运行队列 */
    override fun start() {
        mDelegate.start()
    }

    /** 停止运行队列 */
    override fun stop() {
        mDelegate.stop()
    }

    /** 获取任务列表 */
    override fun getTasks(): List<ITask> {
        return mQueue.toList()
    }

    /** 当前队列大小 */
    override fun getCurrentSize(): Int {
        return mDelegate.getCurrentSize()
    }

    /** 队列是否在运行 */
    override fun isRunning(): Boolean {
        return mDelegate.isRunning()
    }

    /**
     *  清空队列
     *  @param withCurrent 是否包含当前正在运行的弹窗任务（注：为true时也不会自动关闭当前弹窗，只能清除正在运行的任务）
     */
    override fun clear(withCurrent: Boolean) {
        return mDelegate.clear(withCurrent)
    }

}