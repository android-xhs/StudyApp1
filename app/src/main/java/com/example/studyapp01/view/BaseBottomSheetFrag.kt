package com.example.studyapp01.view

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class BaseBottomSheetFrag : BottomSheetDialogFragment() {
    companion object {
        const val TAG = "BaseBottomSheetFragment"
    }

    private lateinit var behavior: BottomSheetBehavior<FrameLayout>
    private var maxHeight = 0
    private var mSlideOffset: Float = 0f
    private val DISMISS_THRESHOLD = 0.2f

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        maxHeight = (resources.displayMetrics.heightPixels * 0.8).toInt()
        setupBehavior()
    }

    private fun setupBehavior() {
        val bottomSheet =
            dialog?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let { view ->
            behavior = BottomSheetBehavior.from(view)
            //设置高度
            view.layoutParams.height = maxHeight
            behavior.peekHeight = maxHeight
            //设置初始状态
            behavior.state = BottomSheetBehavior.STATE_HIDDEN
            //设置是否可以拖动关闭
            behavior.isHideable = true
            //设置是否可以折叠
//            behavior.isFitToContents = false
            //设置跳过折叠状态（直接到半展开）
            behavior.skipCollapsed = true
        }
    }

    private fun setStateListener() {
        var state = ""
        behavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        state = "状态： 完全展开"
                    }

                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        state = "状态： 半展开"
                    }

                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        state = "状态： 折叠"
                    }

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        state = "状态： 隐藏"
                    }

                    BottomSheetBehavior.STATE_DRAGGING -> {
                        state = "状态： 拖动中"
                    }

                    BottomSheetBehavior.STATE_SETTLING -> {
                        state = "状态： 设置中"
                        if (mSlideOffset < -DISMISS_THRESHOLD) {
                            if (isCancelable) {
                                behavior.state = BottomSheetBehavior.STATE_HIDDEN
                            }
                        } else {
                            behavior.state = BottomSheetBehavior.STATE_EXPANDED
                        }
                    }
                }
                Log.i(TAG, "onStateChanged: $state")
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                //滑动过程中的回调
                val percent = (slideOffset * 100).toInt()
                mSlideOffset = slideOffset
                Log.i(TAG, "onSlide: 滑动偏移： $percent")
            }
        })
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //创建自定义的dialog，
        return super.onCreateDialog(savedInstanceState).apply {
            //可以设置dialog的行为，
            setOnShowListener {
                //显示时的处理
                setStateListener()
            }
        }
    }

    fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()
}