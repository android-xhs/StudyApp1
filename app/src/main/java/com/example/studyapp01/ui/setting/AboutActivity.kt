package com.example.studyapp01.ui.setting

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.studyapp01.databinding.AcAboutBinding
import com.example.studyapp01.model.Constant.Companion.TAG

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: AcAboutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: AboutActivity")
        binding = AcAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {

    }
}