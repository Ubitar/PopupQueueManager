package com.ubitar.manager.pqm.demo.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ubitar.manager.pqm.R
import com.ubitar.manager.pqm.databinding.HolderTextBinding
import java.text.SimpleDateFormat
import java.util.*

class HomeTextAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.holder_text) {

    private val mDateFormat=SimpleDateFormat("HH:mm:ss:SSS")

    override fun convert(holder: BaseViewHolder, item: String) {
        val binding = HolderTextBinding.bind(holder.itemView)
        binding.txt.text = "${mDateFormat.format(Date())}-${item}"
    }
}