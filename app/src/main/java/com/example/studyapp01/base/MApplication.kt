package com.example.studyapp01.base

import android.app.Application
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.example.studyapp01.model.Constant
import com.tencent.mmkv.MMKV
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext

class MApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.i(Constant.TAG, "onCreate: App")
        GlobalContext.startKoin {
            androidLogger() // Android 专用日志
            androidContext(this@MApplication) // 提供 Android Context
        }
        MMKV.initialize(this)
        setDayNight()
    }

    //根据保存的值设置模式，dayNight的值为true是白天
    fun setDayNight() {
        val mmkv = MMKV.mmkvWithID("app01")
        val dayNight = mmkv.decodeBool("dayNight")
        AppCompatDelegate.setDefaultNightMode(
            if (dayNight) AppCompatDelegate.MODE_NIGHT_NO
            else AppCompatDelegate.MODE_NIGHT_YES
        )
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.i(Constant.TAG, "onConfigurationChanged: App")
    }
}