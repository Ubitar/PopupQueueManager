package com.ubitar.manager.pqm.demo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ubitar.manager.pqm.databinding.FragmentFunctionBinding

class FunctionFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentFunctionBinding.inflate(inflater, container, false).root
    }

    companion object {
        fun newInstance(): FunctionFragment {
            return FunctionFragment()
        }
    }
}