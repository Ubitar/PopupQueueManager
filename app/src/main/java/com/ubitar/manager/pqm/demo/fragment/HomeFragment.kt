package com.ubitar.manager.pqm.demo.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.ubitar.manager.pqm.PopupQueueManager
import com.ubitar.manager.pqm.databinding.FragmentHomeBinding
import com.ubitar.manager.pqm.demo.adapter.HomeTextAdapter
import com.ubitar.manager.pqm.demo.dialog.ColorDialog
import com.ubitar.manager.pqm.demo.popup.ColorPopup
import com.ubitar.manager.pqm.popup.IQueuePopup
import com.ubitar.manager.pqm.task.QueueTask

class HomeFragment : Fragment() {

    private val mBinding by lazy {
        FragmentHomeBinding.bind(requireView())
    }

    private val mTextAdapter = HomeTextAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentHomeBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.btnOnePopup.setOnClickListener {
            PopupQueueManager.getDefault()
                .pushAndStart(object : QueueTask() {
                    override fun onCreatePopup(): IQueuePopup? {
                        return ColorPopup(requireContext(), Color.RED)
                    }

                    override fun show(popup: IQueuePopup) {
                        showPopup(popup as BasePopupView)
                    }

                })
        }
        mBinding.btnThreePopup.setOnClickListener {
            PopupQueueManager.getDefault()
                .push(object : QueueTask() {
                    override fun onCreatePopup(): IQueuePopup? {
                        return ColorPopup(requireContext(), Color.RED)
                    }

                    override fun show(popup: IQueuePopup) {
                        showPopup(popup as BasePopupView)
                    }

                })
            PopupQueueManager.getDefault()
                .push(object : QueueTask() {
                    override fun onCreatePopup(): IQueuePopup? {
                        return ColorPopup(requireContext(), Color.GREEN)
                    }

                    override fun show(popup: IQueuePopup) {
                        showPopup(popup as BasePopupView)
                    }

                })
            PopupQueueManager.getDefault()
                .push(object : QueueTask() {
                    override fun onCreatePopup(): IQueuePopup? {
                        return ColorPopup(requireContext(), Color.BLUE)
                    }

                    override fun show(popup: IQueuePopup) {
                        showPopup(popup as BasePopupView)
                    }

                })
            PopupQueueManager.getDefault().start()
        }
        mBinding.btnFivePopup.setOnClickListener {
            //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            PopupQueueManager.getDefault().stop()
            PopupQueueManager.getDefault()
                .push(object : QueueTask() {
                    override fun onCreatePopup(): IQueuePopup? {
                        return ColorPopup(requireContext(), Color.RED)
                    }

                    override fun show(popup: IQueuePopup) {
                        showPopup(popup as BasePopupView)
                    }

                    override fun getPriority(): Int = 96
                })
            PopupQueueManager.getDefault()
                .push(object : QueueTask() {
                    override fun onCreatePopup(): IQueuePopup? {
                        return ColorPopup(requireContext(), Color.GREEN)
                    }

                    override fun show(popup: IQueuePopup) {
                        showPopup(popup as BasePopupView)
                    }

                    override fun getPriority(): Int = 97
                })
            PopupQueueManager.getDefault()
                .push(object : QueueTask() {
                    override fun onCreatePopup(): IQueuePopup? {
                        return ColorPopup(requireContext(), Color.BLUE)
                    }

                    override fun show(popup: IQueuePopup) {
                        showPopup(popup as BasePopupView)
                    }
                    override fun getPriority(): Int = 97

                })
            PopupQueueManager.getDefault()
                .push(object : QueueTask() {
                    override fun onCreatePopup(): IQueuePopup? {
                        return ColorPopup(requireContext(), Color.YELLOW)
                    }

                    override fun show(popup: IQueuePopup) {
                        showPopup(popup as BasePopupView)
                    }
                    override fun getPriority(): Int = 97
                })
            PopupQueueManager.getDefault()
                .push(object : QueueTask() {
                    override fun onCreatePopup(): IQueuePopup? {
                        return ColorPopup(requireContext(), Color.GRAY)
                    }

                    override fun show(popup: IQueuePopup) {
                        showPopup(popup as BasePopupView)
                    }
                    override fun getPriority(): Int = 97

                })
            PopupQueueManager.getDefault()
                //???????????????????????????
                .pushAndStart(object : QueueTask() {
                    override fun onCreatePopup(): IQueuePopup? {
                        return ColorPopup(requireContext(), Color.BLACK)
                    }

                    override fun show(popup: IQueuePopup) {
                        showPopup(popup as BasePopupView)
                    }
                    override fun getPriority(): Int = 93
                })
        }
        mBinding.btnClearGroup.setOnClickListener {
            PopupQueueManager.getDefault().clear()
        }
        mBinding.txtClear.setOnClickListener {
            mTextAdapter.setList(arrayListOf())
        }

        mBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        mBinding.recyclerView.adapter = mTextAdapter

        initQueueManager()

    }

    private fun initQueueManager() {
        PopupQueueManager.observeOnAllGroupFinishListener(viewLifecycleOwner) {
            mTextAdapter.addData(0, "???????????????????????????")
            scrollToTop()
        }
        PopupQueueManager.getDefault()
            .observeOnGroupFinishListener(viewLifecycleOwner) {
                mTextAdapter.addData(0, "?????????????????????????????????")
                scrollToTop()
            }
        PopupQueueManager.getDefault()
            //?????????Task????????????????????????
            .observeOnInterruptGroupListener(viewLifecycleOwner) {
                mTextAdapter.addData(0, "????????????????????? ??????false")
                scrollToTop()
                //??????false???????????????????????????????????????true????????????????????????
                false
            }
        PopupQueueManager.getDefault()
            .observeOnInterceptTaskListener(viewLifecycleOwner) { group, task ->
                mTextAdapter.addData(0, "??????????????????????????? ??????false")
                scrollToTop()
                //??????false??????????????????????????????????????????true???????????????????????????
                false
            }
        PopupQueueManager.getDefault()
            .observeOnNextTaskListener(viewLifecycleOwner) { group, task, popup ->
                mTextAdapter.addData(0, "????????????????????????????????????????????????:${group.getCurrentSize()}???????????????????????????")
                scrollToTop()
            }
        PopupQueueManager.getDefault()
            .observeOnBeforeClearListener(viewLifecycleOwner){
                mTextAdapter.addData(0, "???????????????????????????")
                scrollToTop()
            }
        PopupQueueManager.getDefault()
            .observeOnAfterClearListener(viewLifecycleOwner){
                mTextAdapter.addData(0, "???????????????????????????")
                scrollToTop()
            }
    }

    private fun showPopup(popup: BasePopupView) {
        XPopup.Builder(requireContext())
            .dismissOnTouchOutside(false)
            .asCustom(popup)
            .show()
    }

    private fun showDialog(dialog: DialogFragment) {
        dialog.show(childFragmentManager, dialog.tag)
    }

    private fun scrollToTop() {
        mBinding.recyclerView.post {
            (mBinding.recyclerView.layoutManager as LinearLayoutManager).also {
                it.scrollToPosition(0)
            }
        }
    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

}