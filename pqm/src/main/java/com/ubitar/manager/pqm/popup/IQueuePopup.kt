package com.ubitar.manager.pqm.popup

import com.ubitar.manager.pqm.proxy.IQueueProxy

/**
 * 队列中的弹窗
 */
interface IQueuePopup {

    fun onCatchQueueProxy(proxy: IQueueProxy)

}