package com.ubitar.manager.pqm.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ubitar.manager.pqm.databinding.ActivityMainBinding
import com.ubitar.manager.pqm.demo.fragment.FunctionFragment
import com.ubitar.manager.pqm.demo.fragment.HomeFragment

class MainActivity : AppCompatActivity() {

    private val mBinding by lazy {
        ActivityMainBinding.inflate(LayoutInflater.from(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)


        mBinding.imgHome.setOnClickListener {
            mBinding.viewPager.setCurrentItem(0, false)
        }
        mBinding.imgFunction.setOnClickListener {
            Toast.makeText(this,"高级功能演示-装修中",Toast.LENGTH_SHORT).show()
//            mBinding.viewPager.setCurrentItem(1, false)
        }


        mBinding.viewPager.isUserInputEnabled = false
        mBinding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = 2

            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> HomeFragment.newInstance()
                    1 -> FunctionFragment.newInstance()
                    else -> HomeFragment.newInstance()
                }
            }

        }
    }


}