package com.ubitar.manager.pqm.task.base

interface ITaskRetry {

    /** 添加一次重试的次数 */
    fun plushOneRetryCount()

    /**
     *  任务失败
     *  @param currentRetry 当前重试的次数
     *  @param retryCount 最大重试次数
     *  */
    fun onTaskFail(currentRetry: Int, retryCount: Int)

    /** 当前重试的次数 */
    fun getCurrentRetryCount(): Int

    /** 弹窗创建fail后重试的次数 */
    fun getRetryCount(): Int = 3

}