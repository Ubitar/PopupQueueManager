package com.ubitar.manager.pqm.demo.popup

import android.content.Context
import androidx.annotation.ColorInt
import com.lxj.xpopup.core.CenterPopupView
import com.ubitar.manager.pqm.R
import com.ubitar.manager.pqm.databinding.PopupColorBinding
import com.ubitar.manager.pqm.popup.IQueuePopup
import com.ubitar.manager.pqm.proxy.IQueueProxy

class ColorPopup(context: Context, @ColorInt private val color: Int) : CenterPopupView(context), IQueuePopup {

    private val mBinding by lazy { PopupColorBinding.bind(popupImplView) }

    private lateinit var mQueueProxy: IQueueProxy

    override fun getImplLayoutId(): Int = R.layout.popup_color

    override fun onCreate() {
        super.onCreate()
        mBinding.layoutColor.setBackgroundColor(color)
        mBinding.imgClose.setOnClickListener {
            dismiss()
        }
    }

    override fun doAfterDismiss() {
        super.doAfterDismiss()
        //回调通知弹窗关闭
        mQueueProxy.onDismiss()
    }

    override fun onCatchQueueProxy(proxy: IQueueProxy) {
        this.mQueueProxy = proxy
    }

}