package com.zrq.newsdemo.bean

data class News(val msg: String = "",
                val code: Int = 0,
                val newslist: List<NewslistItem>?)