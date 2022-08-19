package com.zrq.newsdemo.network

import com.zrq.newsdemo.bean.News
import com.zrq.newsdemo.utils.Constant.API_KEY
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("/ncov/index?key=$API_KEY")
    fun getNews(): Call<News>
}