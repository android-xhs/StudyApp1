package com.example.studyapp01.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.studyapp01.R
import com.example.studyapp01.dapter.BottomSheetAdapter
import com.example.studyapp01.databinding.FragBottomSheetBinding
import com.example.studyapp01.model.Competition
import com.example.studyapp01.utils.DimensionExt.sp2px
import com.example.studyapp01.view.BaseBottomSheetFrag
import com.example.studyapp01.view.CusSideBarView

class MyBottomSheetDialog : BaseBottomSheetFrag() {
    companion object {
        const val TAG = "MyBottomSheetDialog"
        fun newInstance(): MyBottomSheetDialog = MyBottomSheetDialog()
    }

    private lateinit var binding: FragBottomSheetBinding
    private lateinit var adapter: BottomSheetAdapter
    private var switch: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setSidebar()
    }

    private fun setSidebar() {
        binding.sideBar.apply {
            titles = Competition.titles
            textSize = 12.sp2px
            hotIcon = R.drawable.icon_sidebar_hot
            onIndexSelectedListener = object : CusSideBarView.OnIndexSelectedListener {
                override fun onIndexSelected(index: Int, letter: String, isTouchListener: Boolean) {
                    when (index) {
                        0 -> binding.recyclerView.scrollToPosition(0)
                        else -> {
                            val pos = adapter.datas.indexOfFirst { it.name == letter }
                            (binding.recyclerView.layoutManager as LinearLayoutManager)
                                .scrollToPositionWithOffset(pos, 0)
                        }
                    }
                    if (!isTouchListener)
                        binding.recyclerView.stopScroll()
                }
            }
        }
    }

    private fun setupView() {
        binding.describe.setOnClickListener { setItemAnim(0) }
        binding.close.setOnClickListener { dismiss() }
        adapter = BottomSheetAdapter()
        binding.recyclerView.setItemViewCacheSize(2)
        binding.recyclerView.adapter = adapter
        setItemAnim(0)
        val competitions = mutableListOf<Competition>()
        var idIndex = 0 //标记head在列表数据中的位置
        var tempHeadName = ""//head,暂存以便给所属head之下的数据赋值同样的headName
        for (group in Competition.list1) {
            for (member in group.split("[", "]", ",").map { it.trim() }
                .filter { it.isNotEmpty() }) {
                val competition = Competition()
                with(competition) {
                    if (member == "热门" || member.matches("^[A-Z]\$".toRegex())) {
                        tempHeadName = if (member == "热门") "*" else member
                        headPosition = idIndex
                    }
                    idIndex++
                    headName = tempHeadName
                    name = member
                }
                competitions.add(competition)
            }
        }
        Log.i(TAG, "setupView: $competitions")
        adapter.datas = competitions
        binding.recyclerView.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //通过layoutManager获取recyclerView可见项的位置
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                //获取第一个可见项的位置
                val position = layoutManager.findFirstVisibleItemPosition()
                binding.sideBar.setSelectedTitle(adapter.datas[position].headName)
            }
        })
        adapter.setOnItemClickListener { _, i ->
            adapter.datas[i].selected = !adapter.datas[i].selected
            adapter.notifyItemChanged(i)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setItemAnim(duration: Long) {
        switch = !switch
        val dur = if (switch) 0L else 1000
        binding.describe.text = "${binding.describe.text.toString().substringBefore("=").trim()} = $dur"
        val itemAnimator = DefaultItemAnimator().apply {
            changeDuration = dur  // 默认是 250ms，缩短为 80ms
            moveDuration = 250
            addDuration = 250
            removeDuration = 250
        }
        binding.recyclerView.itemAnimator = itemAnimator
    }
}