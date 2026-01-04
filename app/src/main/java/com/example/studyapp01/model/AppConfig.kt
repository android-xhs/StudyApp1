package com.example.studyapp01.model

/**
 * 示例已经存在，不能像普通类一样写AppConfig()
 */
object AppConfig {
    const val API_BASE_URL = "https://api.example.com"
    var connectionTimeout = 30 //即使是变量，全局也仅此一份
    fun init() {
        println("配置初始化。。。")
    }
}