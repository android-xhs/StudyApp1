package com.example.studyapp01.ui.setting

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.studyapp01.databinding.AcSetttingBinding
import com.tencent.mmkv.MMKV

/**
 * 设置主题：day&night；会重建Activity,
 * 如果想保留Ac状态，需要设置android:configChanges="uiMode",
 * 然后的onConfigChanged回调中实现模式切换的效果
 */
class SettingActivity : AppCompatActivity() {
    private lateinit var binding: AcSetttingBinding
    private val mmkv: MMKV by lazy { MMKV.mmkvWithID("app01") }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AcSetttingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.btnNight.setOnClickListener {
            setDayNight()
        }
    }

    //会重建所有Activity,会调用recreate()
    fun setDayNight() {
        //dayNight的值为true是白天
        val dayNight = !mmkv.decodeBool("dayNight")
        mmkv.encode("dayNight", dayNight)
        AppCompatDelegate.setDefaultNightMode(
            if (dayNight) AppCompatDelegate.MODE_NIGHT_NO
            else AppCompatDelegate.MODE_NIGHT_YES
        )
    }

    //delegate.localNightMode-只重建当前页面的视图
    fun setDayNight2() {
        val dayNight = !mmkv.decodeBool("dayNight")
        mmkv.encode("dayNight", dayNight)
        delegate.localNightMode =
            if (dayNight) AppCompatDelegate.MODE_NIGHT_NO
            else AppCompatDelegate.MODE_NIGHT_YES
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            delegate.applyDayNight()
        }
    }
}