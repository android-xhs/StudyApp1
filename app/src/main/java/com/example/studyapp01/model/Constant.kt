package com.example.studyapp01.model

import android.view.View
import android.view.ViewGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Locale

class Constant {
    companion object {
        const val TAG = "XHS"
    }

    fun test() {
        val names = listOf<String>("qwer", "ccc", "bbb")
        names.map { it.replaceFirstChar(Char::uppercase) }
        names.map { itt ->
            itt.replaceFirstChar {
                if (it.isLowerCase())
                    it.titlecase(Locale.getDefault())
                else
                    it.toString()
            }
        }

        names.flatMap { it.split(",") }
    }
}

// 扩展函数：查找所有指定类型的 View
// View 扩展函数：获取所有子 View（包括嵌套）
fun View.getAllChildren(): Sequence<View> = sequence {
    yield(this@getAllChildren)
    if (this@getAllChildren is ViewGroup) {
        for (i in 0 until this@getAllChildren.childCount) {
            val child = this@getAllChildren.getChildAt(i)
            yieldAll(child.getAllChildren())
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun main() = runBlocking{
    val names = listOf<String>("qwer", "ccc", "bbb")
    val flatMap = names.flatMap { it.split("") }.filter { it.isNotEmpty() }
    val competition = Competition()
    val w = with(competition){
        listOf("")
        "最行一行是返回值"
    }
    ensureActive() //检查取消函数
    val a = async {  }
    a.await()
    val global = GlobalScope.launch {  } //方法体在后台执行，不会阻塞主线程
    val coroutine = CoroutineScope(Dispatchers.Default).launch {  } //不会阻塞线程
    //惰性初始化，直到使用该属性时才进行初始化，并将结果缓存，后续访问直接返回缓存的值。
    //by lazy提供了三种线程安全模式，默认模式、原子操作、非线程安全
    val lazyValue: String by lazy {
        println("computed!")
        "Hello"
    }
}
