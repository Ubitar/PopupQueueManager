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

    override fun onTaskStart() {
        mIsRunning = true
    }

    override fun onTaskRestart() {

    }

    override fun onTaskCancel() {

    }

    override fun onTaskFail(currentRetry: Int, retryCount: Int) {

    }

    override fun onTaskFinish() {

    }

    override fun onTaskComplete() {
        mIsRunning = false
    }

    override fun isRunning(): Boolean = mIsRunning

}