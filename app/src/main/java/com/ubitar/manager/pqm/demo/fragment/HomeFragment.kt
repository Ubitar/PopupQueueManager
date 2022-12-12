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
            //由于有不同优先级的任务，所以此处优暂停队列，否则弹窗队列会优先执行放入的第一个任务，而不是优先执行优先度最高的
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
                //添加任务且开始队列
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
            mTextAdapter.addData(0, "所有队列已播放完毕")
            scrollToTop()
        }
        PopupQueueManager.getDefault()
            .observeOnGroupFinishListener(viewLifecycleOwner) {
                mTextAdapter.addData(0, "当前默认队列已播放完毕")
                scrollToTop()
            }
        PopupQueueManager.getDefault()
            //同一次Task会多次执行此回调
            .observeOnInterruptGroupListener(viewLifecycleOwner) {
                mTextAdapter.addData(0, "是否要终止本组 此次false")
                scrollToTop()
                //返回false时会继续运行弹窗队列，返回true是会停止弹窗队列
                false
            }
        PopupQueueManager.getDefault()
            .observeOnInterceptTaskListener(viewLifecycleOwner) { group, task ->
                mTextAdapter.addData(0, "是否要拦截本次弹窗 此次false")
                scrollToTop()
                //返回false时会继续执行下一个任务，返回true是会跳过下一个任务
                false
            }
        PopupQueueManager.getDefault()
            .observeOnNextTaskListener(viewLifecycleOwner) { group, task, popup ->
                mTextAdapter.addData(0, "开始显示下一弹窗，现队列中的数量:${group.getCurrentSize()}个（包含当前弹窗）")
                scrollToTop()
            }
        PopupQueueManager.getDefault()
            .observeOnBeforeClearListener(viewLifecycleOwner){
                mTextAdapter.addData(0, "清空当前队列前回调")
                scrollToTop()
            }
        PopupQueueManager.getDefault()
            .observeOnAfterClearListener(viewLifecycleOwner){
                mTextAdapter.addData(0, "清空当前队列后回调")
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