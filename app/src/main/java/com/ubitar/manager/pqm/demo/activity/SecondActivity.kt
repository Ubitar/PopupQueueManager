package com.ubitar.manager.pqm.demo.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.ubitar.manager.pqm.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {
    private val mBinding by lazy {
        ActivitySecondBinding.inflate(LayoutInflater.from(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
    }
}