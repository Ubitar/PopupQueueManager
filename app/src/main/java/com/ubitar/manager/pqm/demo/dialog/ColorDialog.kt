package com.ubitar.manager.pqm.demo.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.fragment.app.DialogFragment
import com.ubitar.manager.pqm.databinding.PopupColorBinding
import com.ubitar.manager.pqm.popup.IQueuePopup
import com.ubitar.manager.pqm.proxy.IQueueProxy

class ColorDialog(
    @ColorInt private val color: Int
) : DialogFragment(), IQueuePopup {

    private val mBinding by lazy { PopupColorBinding.bind(requireView()) }

    private lateinit var mQueueProxy: IQueueProxy

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return PopupColorBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.layoutColor.setBackgroundColor(color)
        mBinding.imgClose.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        //回调通知弹窗关闭
        mQueueProxy.onDismiss()
    }

    override fun onCatchQueueProxy(proxy: IQueueProxy) {
        this.mQueueProxy = proxy
    }

}