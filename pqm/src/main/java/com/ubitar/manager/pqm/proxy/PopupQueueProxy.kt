package com.ubitar.manager.pqm.proxy

open class PopupQueueProxy(
    private val mOnDismiss: () -> Unit
) : IQueueProxy {
    override fun onDismiss() {
        mOnDismiss.invoke()
    }
}