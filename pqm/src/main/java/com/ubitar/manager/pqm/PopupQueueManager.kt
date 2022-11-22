package com.ubitar.manager.pqm

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.ubitar.manager.pqm.group.Groups
import com.ubitar.manager.pqm.group.IGroup
import com.ubitar.manager.pqm.group.LinkedQueueGroup
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

object PopupQueueManager {

    private var mIsStopAfterFinish = false
    private val mGroupMap = ConcurrentHashMap<Groups, IGroup>()

    private val mOnAllGroupFinishListeners = CopyOnWriteArrayList<Pair<LifecycleOwner?, () -> Unit>>()

    fun getDefault(): IGroup {
        return get(Groups.defaultGroup())
    }

    fun get(groups: Groups): IGroup {
        return mGroupMap.getOrPut(groups) {
            onCreateNewGroups()
        }
    }

    fun addOnAllGroupFinishListener(listener: () -> Unit) {
        removeOnAllGroupFinishListener(listener)
        mOnAllGroupFinishListeners.add(Pair(null, listener))
    }

    fun removeOnAllGroupFinishListener(listener: () -> Unit) {
        val index = mOnAllGroupFinishListeners.indexOfFirst { it.second == listener }
        if (index < 0) return
        mOnAllGroupFinishListeners.removeAt(index)
    }

    fun observeOnAllGroupFinishListener(lifecycleOwner: LifecycleOwner, listener: () -> Unit) {
        removeObserveOnAllGroupFinish(lifecycleOwner)
        removeObserveWhenDestroy(lifecycleOwner) {
            removeObserveOnAllGroupFinish(lifecycleOwner)
        }
        mOnAllGroupFinishListeners.add(Pair(lifecycleOwner, listener))
    }

    fun removeObserveOnAllGroupFinish(lifecycleOwner: LifecycleOwner) {
        val index = mOnAllGroupFinishListeners.indexOfFirst { it.first == lifecycleOwner }
        if (index < 0) return
        mOnAllGroupFinishListeners.removeAt(index)
    }

    fun setStopAfterFinish(isStop: Boolean) {
        mIsStopAfterFinish = isStop
    }

    fun getStopAfterFinish(): Boolean {
        return mIsStopAfterFinish
    }

    fun clearAll() {
        mGroupMap.forEach {
            it.value.clear()
        }
    }

    private fun onCreateNewGroups(): IGroup {
        val group = LinkedQueueGroup()
        group.addOnGroupFinishListener {
            val isAllFinish = mGroupMap.all { it.value.getCurrentSize() <=0 }
            if (isAllFinish) {
                mOnAllGroupFinishListeners.forEach {
                    it.second.invoke()
                }
            }
        }
        return group
    }

    private fun removeObserveWhenDestroy(lifecycleOwner: LifecycleOwner, action: () -> Unit) {
        lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                action.invoke()
            }
        })
    }
}