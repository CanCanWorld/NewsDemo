package com.zrq.newsdemo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.zrq.newsdemo.repository.NewsRepository
import com.zrq.newsdemo.utils.Constant.BASE_URL
import com.zrq.newsdemo.utils.Constant.GET_NEWS
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_test.setOnClickListener {
            NewsRepository.getNews().observe(this@MainActivity) { result ->
                val news = result.getOrNull()
                if (news != null) {
                    Log.d("TAG", "onCreate: ${news.code}")
                    Log.d("TAG", "onCreate: ${news.msg}")
                    Log.d("TAG", "onCreate: ${news.newslist.toString()}")
                } else {
                    Log.d("TAG", "NULL:$result")
                }
            }
        }
    }
}