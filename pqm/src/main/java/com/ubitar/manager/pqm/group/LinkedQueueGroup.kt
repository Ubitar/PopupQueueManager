package com.ubitar.manager.pqm.group

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.ubitar.manager.pqm.delegate.IDelegate
import com.ubitar.manager.pqm.delegate.QueueDelegate
import com.ubitar.manager.pqm.popup.IQueuePopup
import com.ubitar.manager.pqm.task.base.ITask
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.CopyOnWriteArrayList

class LinkedQueueGroup : IGroup {

    private val mOnGroupFinishListeners = CopyOnWriteArrayList<Pair<LifecycleOwner?, (group: IGroup) -> Unit>>()
    private val mOnInterruptGroupListeners = CopyOnWriteArrayList<Pair<LifecycleOwner?, (group: IGroup) -> Boolean>>()
    private val mOnInterceptTaskListeners = CopyOnWriteArrayList<Pair<LifecycleOwner?, (group: IGroup, task: ITask) -> Boolean>>()
    private val mOnNextTaskListeners = CopyOnWriteArrayList<Pair<LifecycleOwner?, (group: IGroup, task: ITask, popup: IQueuePopup) -> Unit>>()

    private var mBeforeDelay = -1L
    private var mAfterDelay = -1L
    private var mIsStopAfterFinish = false
    private val mQueue: Queue<ITask> = ConcurrentLinkedQueue() //(线程安全)
    private val mDelegate: IDelegate by lazy {
        QueueDelegate(
            mGroup = this,
            mQueue = mQueue,
            mOnGroupFinishListeners = mOnGroupFinishListeners,
            mOnInterruptGroupListeners = mOnInterruptGroupListeners,
            mOnInterceptTaskListeners = mOnInterceptTaskListeners,
            mOnNextTaskListeners = mOnNextTaskListeners
        )
    }

    override fun addOnGroupFinishListener(listener: (group: IGroup) -> Unit) {
        removeOnGroupFinishListener(listener)
        mOnGroupFinishListeners.add(Pair(null, listener))
    }

    override fun addOnInterruptGroupListener(listener: (group: IGroup) -> Boolean) {
        removeOnInterruptGroupListener(listener)
        mOnInterruptGroupListeners.add(Pair(null, listener))
    }

    override fun addOnInterceptTaskListener(listener: (group: IGroup, task: ITask) -> Boolean) {
        removeOnInterceptTaskListener(listener)
        mOnInterceptTaskListeners.add(Pair(null, listener))
    }

    override fun addOnNextTaskListener(listener: (group: IGroup, task: ITask, popup: IQueuePopup) -> Unit) {
        removeOnNextTaskListener(listener)
        mOnNextTaskListeners.add(Pair(null, listener))
    }

    override fun removeOnGroupFinishListener(listener: (group: IGroup) -> Unit) {
        val index = mOnGroupFinishListeners.indexOfFirst { it.second == listener }
        if (index < 0) return
        mOnGroupFinishListeners.removeAt(index)
    }

    override fun removeOnInterruptGroupListener(listener: (group: IGroup) -> Boolean) {
        val index = mOnInterruptGroupListeners.indexOfFirst { it.second == listener }
        if (index < 0) return
        mOnInterruptGroupListeners.removeAt(index)
    }

    override fun removeOnInterceptTaskListener(listener: (group: IGroup, task: ITask) -> Boolean) {
        val index = mOnInterceptTaskListeners.indexOfFirst { it.second == listener }
        if (index < 0) return
        mOnInterceptTaskListeners.removeAt(index)
    }

    override fun removeOnNextTaskListener(listener: (group: IGroup, task: ITask, popup: IQueuePopup) -> Unit) {
        val index = mOnNextTaskListeners.indexOfFirst { it.second == listener }
        if (index < 0) return
        mOnNextTaskListeners.removeAt(index)
    }

    override fun observeOnGroupFinishListener(lifecycleOwner: LifecycleOwner, listener: (group: IGroup) -> Unit) {
        removeObserveOnGroupFinishListener(lifecycleOwner)
        removeObserveWhenDestroy(lifecycleOwner) {
            removeObserveOnGroupFinishListener(lifecycleOwner)
        }
        mOnGroupFinishListeners.add(Pair(lifecycleOwner, listener))
    }

    override fun observeOnInterruptGroupListener(lifecycleOwner: LifecycleOwner, listener: (group: IGroup) -> Boolean) {
        removeObserveOnInterruptGroupListener(lifecycleOwner)
        removeObserveWhenDestroy(lifecycleOwner) {
            removeObserveOnInterruptGroupListener(lifecycleOwner)
        }
        mOnInterruptGroupListeners.add(Pair(lifecycleOwner, listener))
    }

    override fun observeOnInterceptTaskListener(lifecycleOwner: LifecycleOwner, listener: (group: IGroup, task: ITask) -> Boolean) {
        removeObserveOnInterceptTaskListener(lifecycleOwner)
        removeObserveWhenDestroy(lifecycleOwner) {
            removeObserveOnInterceptTaskListener(lifecycleOwner)
        }
        mOnInterceptTaskListeners.add(Pair(lifecycleOwner, listener))
    }

    override fun observeOnNextTaskListener(lifecycleOwner: LifecycleOwner, listener: (group: IGroup, task: ITask, popup: IQueuePopup) -> Unit) {
        removeObserveOnNextTaskListener(lifecycleOwner)
        removeObserveWhenDestroy(lifecycleOwner) {
            removeObserveOnNextTaskListener(lifecycleOwner)
        }
        mOnNextTaskListeners.add(Pair(lifecycleOwner, listener))
    }

    override fun removeObserveOnGroupFinishListener(lifecycleOwner: LifecycleOwner) {
        val index = mOnGroupFinishListeners.indexOfFirst { it.first == lifecycleOwner }
        if (index < 0) return
        mOnGroupFinishListeners.removeAt(index)
    }

    override fun removeObserveOnInterruptGroupListener(lifecycleOwner: LifecycleOwner) {
        val index = mOnInterruptGroupListeners.indexOfFirst { it.first == lifecycleOwner }
        if (index < 0) return
        mOnInterruptGroupListeners.removeAt(index)
    }

    override fun removeObserveOnInterceptTaskListener(lifecycleOwner: LifecycleOwner) {
        val index = mOnInterceptTaskListeners.indexOfFirst { it.first == lifecycleOwner }
        if (index < 0) return
        mOnInterceptTaskListeners.removeAt(index)
    }

    override fun removeObserveOnNextTaskListener(lifecycleOwner: LifecycleOwner) {
        val index = mOnNextTaskListeners.indexOfFirst { it.first == lifecycleOwner }
        if (index < 0) return
        mOnNextTaskListeners.removeAt(index)
    }

    override fun push(task: ITask) {
        mDelegate.push(task)
    }

    override fun pushAndStart(task: ITask) {
        mDelegate.pushAndStart(task)
    }

    override fun setBeforeTaskDelay(delay: Long) {
        this.mBeforeDelay = delay
    }

    override fun setAfterTaskDelay(delay: Long) {
        this.mAfterDelay = delay
    }

    override fun getBeforeTaskDelay(): Long {
        return mBeforeDelay
    }

    override fun getAfterTaskDelay(): Long {
        return mAfterDelay
    }

    override fun setStopAfterFinish(isStop: Boolean) {
        mIsStopAfterFinish = isStop
    }

    override fun getStopAfterFinish(): Boolean {
        return mIsStopAfterFinish
    }

    override fun start() {
        mDelegate.start()
    }

    override fun stop() {
        mDelegate.stop()
    }

    override fun getTasks(): List<ITask> {
        return mQueue.toList()
    }

    override fun getCurrentSize(): Int {
        return mDelegate.getCurrentSize()
    }

    override fun isRunning(): Boolean {
        return mDelegate.isRunning()
    }

    override fun clear() {
        mDelegate.clear()
    }

    private fun removeObserveWhenDestroy(lifecycleOwner: LifecycleOwner, action: () -> Unit) {
        lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                action.invoke()
            }
        })
    }

}