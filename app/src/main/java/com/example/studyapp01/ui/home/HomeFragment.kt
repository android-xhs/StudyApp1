package com.example.studyapp01.ui.home

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.studyapp01.R
import com.example.studyapp01.dapter.BottomSheetAdapter
import com.example.studyapp01.databinding.FragHomeBinding
import com.example.studyapp01.model.Competition
import com.example.studyapp01.model.getAllChildren
import com.example.studyapp01.ui.setting.AboutActivity
import com.example.studyapp01.ui.setting.SettingActivity
import com.example.studyapp01.viewmodel.HomeViewModel
import com.tencent.mmkv.MMKV

class HomeFragment : Fragment() {
    private lateinit var binding: FragHomeBinding
    private lateinit var adapter: BottomSheetAdapter
    private val viewModel: HomeViewModel by viewModels<HomeViewModel>()
    private val mmkv: MMKV by lazy { MMKV.mmkvWithID("app01") }
    private val TAG = "XHS"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewModelProvider.create(this)[HomeViewModel::class.java]
        initView()
    }

    private fun initView() {
        setupListener()
        for (i in 0..10) {
            val newTab = binding.tabLayout.newTab()
            newTab.text = "Tab$i"
            binding.tabLayout.addTab(newTab)
        }
        viewModel.mData.observe(viewLifecycleOwner) {
            Log.i(TAG, "initView: viewModel = $it")
            binding.title.text = it
        }
        binding.rv.setItemViewCacheSize(30)
        adapter = BottomSheetAdapter()
        binding.rv.adapter = adapter
        setupDatas()
    }

    private fun setupDatas() {
        val competitions = mutableListOf<Competition>()
        var idIndex = 0 //标记head在列表数据中的位置
        var tempHeadName = ""//head,暂存以便给所属head之下的数据赋值同样的headName-字母
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
                    id = 100
                }
                competitions.add(competition)
            }
        }
        Log.i(MyBottomSheetDialog.Companion.TAG, "setupView: $competitions")
        adapter.datas = competitions
    }

    private fun setupListener() {
        val dialog = MyBottomSheetDialog.newInstance()
        binding.btnPop.setOnClickListener {
            if (!dialog.isVisible)
                dialog.showNow(requireActivity().supportFragmentManager, MyBottomSheetDialog.TAG)
        }

        binding.btnNight.setOnClickListener {
            startActivity(Intent(requireActivity(), SettingActivity::class.java))
        }
        binding.btnAbout.setOnClickListener {
            startActivity(Intent(requireActivity(), AboutActivity::class.java))
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES) {
            //当前已切换到深色模式，切换UI显示
        }
        // 检测夜间模式变化
        val oldNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val newNightMode = newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (oldNightMode != newNightMode) {
            val isNightMode = newNightMode == Configuration.UI_MODE_NIGHT_YES
            // 使用新的兼容方案
//            updateSystemBarsModern(isNightMode)
        }

        binding.main.setBackgroundColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.main_bg_color
            )
        )
        val allTextViews = binding.main.getAllChildren()
            .filterIsInstance<TextView>()
            .toList()
        allTextViews.forEach {
            it.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.text_color
                )
            )
            it.setBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.main_bg_color
                )
            )
        }
        binding.rv.adapter?.notifyDataSetChanged()
    }
}