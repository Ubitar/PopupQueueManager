package com.ubitar.manager.pqm.task

import com.ubitar.manager.pqm.task.base.IAsyncTask

abstract class QueueAsyncTask : IAsyncTask {

    private var mCurrentRetryCount = 0

    private var mIsRunning = false

    override fun plushOneRetryCount() {
        mCurrentRetryCount++
    }

    override fun getCurrentRetryCount(): Int {
        return mCurrentRetryCount
    }

    override fun getRetryCount(): Int {
        return super.getRetryCount()
    }

    override fun onQueuePeek() {
        mIsRunning = true
    }

    override fun onQueuePoll() {
        mIsRunning = false
    }

    override fun isRunning(): Boolean = mIsRunning

}