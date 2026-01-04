package com.example.studyapp01.base

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.studyapp01.R
import com.example.studyapp01.databinding.AcMainBinding
import com.example.studyapp01.ui.home.HomeFragment
import com.example.studyapp01.ui.mine.MineFragment
import com.example.studyapp01.ui.notice.NoticeFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tencent.mmkv.MMKV

/**
 * 1.noActionBar + enableEdgeToEdge + setOnApplyWindowInsetsListener
 * 2.viewBinding + material.TabLayout + viewPager + FragmentStateAdapter
 *
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: AcMainBinding
    private val TAG = "XHS"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDayNight()
        Log.i(TAG, "onCreate: MainActivity")
        enableEdgeToEdge()
        binding = AcMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setContentPadding()
        initView()
    }
    fun setDayNight() {
        val mmkv = MMKV.mmkvWithID("app01")
        val dayNight = mmkv.decodeBool("dayNight")
        AppCompatDelegate.setDefaultNightMode(
            if (dayNight) AppCompatDelegate.MODE_NIGHT_NO
            else AppCompatDelegate.MODE_NIGHT_YES
        )
    }

    private fun initView() {
//        supportFragmentManager.beginTransaction().add(R.id.main, HomeFragment()).commit()
        setPager()
    }

    private fun setPager() {
        val tabTitles = listOf("首页", "发现", "我的")
        val fragList = mutableListOf<Fragment>()
        fragList.add(HomeFragment())
        fragList.add(NoticeFragment())
        fragList.add(MineFragment())

        //1.设置ViewPager的Adapter
        binding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = fragList.size

            override fun createFragment(position: Int): Fragment = fragList[position]
        }
        //2.链接TabLayout和Viewpage
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
            //可以自定义视图
//            tab.setCustomView(R.layout.frag_home)
        }.attach()
        //3.设置TabLayout,监听tab选择
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

        })
    }

    private fun setContentPadding() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    fun getNightColor(context: Context, @ColorRes colorRes: Int): Int {
        val configuration = Configuration(context.resources.configuration)
        configuration.uiMode = Configuration.UI_MODE_NIGHT_YES or (configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK.inv())
        val nightContext = context.createConfigurationContext(configuration)
        return ContextCompat.getColor(nightContext, colorRes)
    }

    fun getDayColor(context: Context, @ColorRes colorRes: Int): Int {
        val configuration = Configuration(context.resources.configuration)
        configuration.uiMode = Configuration.UI_MODE_NIGHT_NO or (configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK.inv())
        val dayContext = context.createConfigurationContext(configuration)
        return ContextCompat.getColor(dayContext, colorRes)
    }

}
