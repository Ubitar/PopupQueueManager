package com.ubitar.manager.pqm.group

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.ubitar.manager.pqm.popup.IQueuePopup
import com.ubitar.manager.pqm.task.base.ITask
import java.util.concurrent.CopyOnWriteArrayList

abstract class QueueGroup : IGroup, IGroupListeners {

    internal val mOnGroupFinishListeners = CopyOnWriteArrayList<Pair<LifecycleOwner?, (group: IGroup) -> Unit>>()
    internal val mOnInterruptGroupListeners = CopyOnWriteArrayList<Pair<LifecycleOwner?, (group: IGroup) -> Boolean>>()
    internal val mOnInterceptTaskListeners = CopyOnWriteArrayList<Pair<LifecycleOwner?, (group: IGroup, task: ITask) -> Boolean>>()
    internal val mOnNextTaskListeners = CopyOnWriteArrayList<Pair<LifecycleOwner?, (group: IGroup, task: ITask, popup: IQueuePopup) -> Unit>>()
    internal val mOnBeforeClearListeners = CopyOnWriteArrayList<Pair<LifecycleOwner?, (group: IGroup) -> Unit>>()
    internal val mOnAfterClearListeners = CopyOnWriteArrayList<Pair<LifecycleOwner?, (group: IGroup) -> Unit>>()

    /** 添加分组播放结束的监听 */
    override fun addOnGroupFinishListener(listener: (group: IGroup) -> Unit) {
        removeOnGroupFinishListener(listener)
        mOnGroupFinishListeners.add(Pair(null, listener))
    }

    /** 添加终止分组的监听 */
    override fun addOnInterruptGroupListener(listener: (group: IGroup) -> Boolean) {
        removeOnInterruptGroupListener(listener)
        mOnInterruptGroupListeners.add(Pair(null, listener))
    }

    /** 添加拦截本次任务的监听 */
    override fun addOnInterceptTaskListener(listener: (group: IGroup, task: ITask) -> Boolean) {
        removeOnInterceptTaskListener(listener)
        mOnInterceptTaskListeners.add(Pair(null, listener))
    }

    /** 添加开始下一个任务的监听 */
    override fun addOnNextTaskListener(listener: (group: IGroup, task: ITask, popup: IQueuePopup) -> Unit) {
        removeOnNextTaskListener(listener)
        mOnNextTaskListeners.add(Pair(null, listener))
    }

    /** 添加清除队列前的监听 */
    override fun addOnBeforeClearListener(listener: (group: IGroup) -> Unit) {
        removeOnBeforeClearListener(listener)
        mOnBeforeClearListeners.add(Pair(null, listener))
    }

    /** 添加清除队列后的监听 */
    override fun addOnAfterClearListener(listener: (group: IGroup) -> Unit) {
        removeOnAfterClearListener(listener)
        mOnAfterClearListeners.add(Pair(null, listener))
    }

    /** 移除分组播放结束的监听 */
    override fun removeOnGroupFinishListener(listener: (group: IGroup) -> Unit) {
        val index = mOnGroupFinishListeners.indexOfFirst { it.second == listener }
        if (index < 0) return
        mOnGroupFinishListeners.removeAt(index)
    }

    /** 移除终止分组的监听 */
    override fun removeOnInterruptGroupListener(listener: (group: IGroup) -> Boolean) {
        val index = mOnInterruptGroupListeners.indexOfFirst { it.second == listener }
        if (index < 0) return
        mOnInterruptGroupListeners.removeAt(index)
    }

    /** 移除拦截本次任务的监听 */
    override fun removeOnInterceptTaskListener(listener: (group: IGroup, task: ITask) -> Boolean) {
        val index = mOnInterceptTaskListeners.indexOfFirst { it.second == listener }
        if (index < 0) return
        mOnInterceptTaskListeners.removeAt(index)
    }

    /** 移除开始下一个任务的监听 */
    override fun removeOnNextTaskListener(listener: (group: IGroup, task: ITask, popup: IQueuePopup) -> Unit) {
        val index = mOnNextTaskListeners.indexOfFirst { it.second == listener }
        if (index < 0) return
        mOnNextTaskListeners.removeAt(index)
    }

    /** 移除清除队列前的监听 */
    override fun removeOnBeforeClearListener(listener: (group: IGroup) -> Unit) {
        val index = mOnBeforeClearListeners.indexOfFirst { it.second == listener }
        if (index < 0) return
        mOnBeforeClearListeners.removeAt(index)
    }

    /** 移除清除队列后的监听 */
    override fun removeOnAfterClearListener(listener: (group: IGroup) -> Unit) {
        val index = mOnAfterClearListeners.indexOfFirst { it.second == listener }
        if (index < 0) return
        mOnAfterClearListeners.removeAt(index)
    }

    /** 添加分组播放结束的监听 */
    override fun observeOnGroupFinishListener(lifecycleOwner: LifecycleOwner, listener: (group: IGroup) -> Unit) {
        removeObserveOnGroupFinishListener(lifecycleOwner)
        removeObserveWhenDestroy(lifecycleOwner) {
            removeObserveOnGroupFinishListener(lifecycleOwner)
        }
        mOnGroupFinishListeners.add(Pair(lifecycleOwner, listener))
    }

    /** 添加终止分组的监听 */
    override fun observeOnInterruptGroupListener(lifecycleOwner: LifecycleOwner, listener: (group: IGroup) -> Boolean) {
        removeObserveOnInterruptGroupListener(lifecycleOwner)
        removeObserveWhenDestroy(lifecycleOwner) {
            removeObserveOnInterruptGroupListener(lifecycleOwner)
        }
        mOnInterruptGroupListeners.add(Pair(lifecycleOwner, listener))
    }

    /** 添加拦截本次任务的监听 */
    override fun observeOnInterceptTaskListener(lifecycleOwner: LifecycleOwner, listener: (group: IGroup, task: ITask) -> Boolean) {
        removeObserveOnInterceptTaskListener(lifecycleOwner)
        removeObserveWhenDestroy(lifecycleOwner) {
            removeObserveOnInterceptTaskListener(lifecycleOwner)
        }
        mOnInterceptTaskListeners.add(Pair(lifecycleOwner, listener))
    }

    /** 添加开始下一个任务的监听 */
    override fun observeOnNextTaskListener(lifecycleOwner: LifecycleOwner, listener: (group: IGroup, task: ITask, popup: IQueuePopup) -> Unit) {
        removeObserveOnNextTaskListener(lifecycleOwner)
        removeObserveWhenDestroy(lifecycleOwner) {
            removeObserveOnNextTaskListener(lifecycleOwner)
        }
        mOnNextTaskListeners.add(Pair(lifecycleOwner, listener))
    }

    /** 添加清除队列前的监听 */
    override fun observeOnBeforeClearListener(lifecycleOwner: LifecycleOwner, listener: (group: IGroup) -> Unit) {
        removeObserveOnBeforeClearListener(lifecycleOwner)
        removeObserveWhenDestroy(lifecycleOwner) {
            removeObserveOnBeforeClearListener(lifecycleOwner)
        }
        mOnBeforeClearListeners.add(Pair(lifecycleOwner, listener))
    }

    /** 添加清除队列后的监听 */
    override fun observeOnAfterClearListener(lifecycleOwner: LifecycleOwner, listener: (group: IGroup) -> Unit) {
        removeObserveOnAfterClearListener(lifecycleOwner)
        removeObserveWhenDestroy(lifecycleOwner) {
            removeObserveOnAfterClearListener(lifecycleOwner)
        }
        mOnAfterClearListeners.add(Pair(lifecycleOwner, listener))
    }

    /** 移除分组播放结束的监听 */
    override fun removeObserveOnGroupFinishListener(lifecycleOwner: LifecycleOwner) {
        val index = mOnGroupFinishListeners.indexOfFirst { it.first == lifecycleOwner }
        if (index < 0) return
        mOnGroupFinishListeners.removeAt(index)
    }

    /** 移除终止分组的监听 */
    override fun removeObserveOnInterruptGroupListener(lifecycleOwner: LifecycleOwner) {
        val index = mOnInterruptGroupListeners.indexOfFirst { it.first == lifecycleOwner }
        if (index < 0) return
        mOnInterruptGroupListeners.removeAt(index)
    }

    /** 移除拦截本次任务的监听 */
    override fun removeObserveOnInterceptTaskListener(lifecycleOwner: LifecycleOwner) {
        val index = mOnInterceptTaskListeners.indexOfFirst { it.first == lifecycleOwner }
        if (index < 0) return
        mOnInterceptTaskListeners.removeAt(index)
    }

    /** 移除开始下一个任务的监听 */
    override fun removeObserveOnNextTaskListener(lifecycleOwner: LifecycleOwner) {
        val index = mOnNextTaskListeners.indexOfFirst { it.first == lifecycleOwner }
        if (index < 0) return
        mOnNextTaskListeners.removeAt(index)
    }

    /** 移除清除队列前的监听 */
    override fun removeObserveOnBeforeClearListener(lifecycleOwner: LifecycleOwner) {
        val index = mOnBeforeClearListeners.indexOfFirst { it.first == lifecycleOwner }
        if (index < 0) return
        mOnBeforeClearListeners.removeAt(index)
    }

    /** 移除清除队列后的监听 */
    override fun removeObserveOnAfterClearListener(lifecycleOwner: LifecycleOwner) {
        val index = mOnAfterClearListeners.indexOfFirst { it.first == lifecycleOwner }
        if (index < 0) return
        mOnAfterClearListeners.removeAt(index)
    }

    /** 添加生命周期Destroy时移除监听的逻辑 */
    private fun removeObserveWhenDestroy(lifecycleOwner: LifecycleOwner, action: () -> Unit) {
        lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                action.invoke()
            }
        })
    }


}