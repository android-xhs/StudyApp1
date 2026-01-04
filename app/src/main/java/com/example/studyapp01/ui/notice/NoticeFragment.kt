package com.example.studyapp01.ui.notice

import android.animation.Animator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.studyapp01.databinding.FragNoticeBinding
import com.example.studyapp01.model.Constant.Companion.TAG
import com.xhs.mylibrary.add
import com.xhs.mylibrary.dev
import com.xhs.mylibrary.toast

class NoticeFragment: Fragment() {
    private lateinit var binding: FragNoticeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragNoticeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvTestAar.text = "新项目调用".dev()
        binding.tvTestAar.setOnClickListener {
            "我是一个来自库的弹窗".toast(requireActivity())
        }
    }

    private fun setupAnim() {
        binding.lottieAnimView.apply {
            //动画播放控制
//            playAnimation()
//            pauseAnimation()
//            resumeAnimation()
//            cancelAnimation()
            //监听器
            addAnimatorUpdateListener { animation ->
                val progress = animation.animatedValue as Float
                Log.i(TAG, "setupAnim: progress = $progress")
            }
            addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {}

                override fun onAnimationCancel(animation: Animator) {}

                override fun onAnimationRepeat(animation: Animator) {}
            })
        }
    }
}