package com.ubitar.manager.pqm.task

import com.ubitar.manager.pqm.task.base.ISyncTask

abstract class QueueTask : ISyncTask {

    private var mIsRunning = false

    override fun onTaskStart() {
        mIsRunning = true
    }

    override fun onTaskRestart() {

    }

    override fun onTaskCancel() {

    }

    override fun onTaskFinish() {

    }

    override fun onTaskComplete() {
        mIsRunning = false
    }

    override fun isRunning(): Boolean = mIsRunning

}