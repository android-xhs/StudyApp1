package com.example.studyapp01.base

import com.xhs.mylibrary2.BaseActivity

class TestActivity: BaseActivity() {
    override fun initView() {
        println("为变基而生")
    }
}