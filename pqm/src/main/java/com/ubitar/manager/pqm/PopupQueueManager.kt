package com.ubitar.manager.pqm

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.ubitar.manager.pqm.group.Groups
import com.ubitar.manager.pqm.group.IGroup
import com.ubitar.manager.pqm.group.LinkedQueueGroup
import com.ubitar.manager.pqm.group.QueueGroup
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

object PopupQueueManager {

    /** 是否队列播放结束后停止队列 */
    private var mIsStopAfterFinish = false

    /** 分组列表 */
    private val mGroupMap = ConcurrentHashMap<Groups, QueueGroup>()

    /** 所有分组队列播放结束的监听列表 */
    private val mOnAllGroupFinishListeners = CopyOnWriteArrayList<Pair<LifecycleOwner?, () -> Unit>>()

    /** 获取默认分组 */
    fun getDefault(): QueueGroup {
        return get(Groups.defaultGroup())
    }

    /** 获取指定分组 */
    fun get(groups: Groups): QueueGroup {
        return mGroupMap.getOrPut(groups) {
            onCreateNewGroups()
        }
    }

    /** 添加所有组播放结束的监听 */
    fun addOnAllGroupFinishListener(listener: () -> Unit) {
        removeOnAllGroupFinishListener(listener)
        mOnAllGroupFinishListeners.add(Pair(null, listener))
    }

    /** 移除所有组播放结束的监听 */
    fun removeOnAllGroupFinishListener(listener: () -> Unit) {
        val index = mOnAllGroupFinishListeners.indexOfFirst { it.second == listener }
        if (index < 0) return
        mOnAllGroupFinishListeners.removeAt(index)
    }

    /** 添加所有组播放结束的监听 */
    fun observeOnAllGroupFinishListener(lifecycleOwner: LifecycleOwner, listener: () -> Unit) {
        removeObserveOnAllGroupFinishListener(lifecycleOwner)
        removeObserveWhenDestroy(lifecycleOwner) {
            removeObserveOnAllGroupFinishListener(lifecycleOwner)
        }
        mOnAllGroupFinishListeners.add(Pair(lifecycleOwner, listener))
    }

    /** 移除所有组播放结束的监听 */
    fun removeObserveOnAllGroupFinishListener(lifecycleOwner: LifecycleOwner) {
        val index = mOnAllGroupFinishListeners.indexOfFirst { it.first == lifecycleOwner }
        if (index < 0) return
        mOnAllGroupFinishListeners.removeAt(index)
    }

    /** 设置所有分组播放结束后就停止 */
    fun setStopAfterFinish(isStop: Boolean) {
        mIsStopAfterFinish = isStop
    }

    /** 获取所有分组播放结束后就停止 */
    fun getStopAfterFinish(): Boolean {
        return mIsStopAfterFinish
    }

    /** 清除所有分组队列 */
    fun clearAll() {
        mGroupMap.forEach {
            it.value.clear()
        }
    }

    /** 创建新的分组 */
    private fun onCreateNewGroups(): QueueGroup {
        val group = LinkedQueueGroup()
        //添加队列结束的监听
        group.addOnGroupFinishListener {
            val isAllFinish = mGroupMap.all { it.value.getCurrentSize() <= 0 }
            if (isAllFinish) {
                mOnAllGroupFinishListeners.forEach {
                    it.second.invoke()
                }
            }
        }
        return group
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