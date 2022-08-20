package com.zrq.newsdemo.repository

import com.zrq.newsdemo.network.NetworkRequest
import com.zrq.newsdemo.utils.Constant.CODE
import kotlinx.coroutines.Dispatchers

object NewsRepository : BaseRepository() {
    fun getNews() = fire(Dispatchers.IO) {
        val news = NetworkRequest.getNews()
        if (news.code == CODE) Result.success(news)
        else Result.failure(RuntimeException("getNews response code is ${news.code} msg is ${news.msg}"))
    }
}