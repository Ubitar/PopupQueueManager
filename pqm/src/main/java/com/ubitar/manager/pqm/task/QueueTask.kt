package com.ubitar.manager.pqm.task

import com.ubitar.manager.pqm.task.base.ISyncTask

abstract class QueueTask : ISyncTask {

    private var mIsRunning = false

    override fun onQueuePeek() {
        mIsRunning = true
    }

    override fun onQueuePoll() {
        mIsRunning = false
    }

    override fun isRunning(): Boolean = mIsRunning

}