package com.zrq.newsdemo

import android.content.Intent
import android.os.*
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.zrq.newsdemo.bean.News
import com.zrq.newsdemo.bean.NewslistItem
import com.zrq.newsdemo.databinding.ActivityMainBinding
import com.zrq.newsdemo.databinding.ItemNewsBinding
import com.zrq.newsdemo.utils.Constant.BASE_URL
import com.zrq.newsdemo.utils.Constant.GET_NEWS
import com.zrq.newsdemo.utils.Constant.SUCCESS
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var newsList = mutableListOf<NewslistItem>()
    private lateinit var adapter: Adapter<NewslistItem, ItemNewsBinding>
    private lateinit var autoTransition: AutoTransition
    private var page: Int = 1
    private lateinit var word: String
    private var isSearch = false
    private var handler = Handler(Looper.myLooper()!!) { msg ->
        if (msg.what == 1)
            adapter.addNews(newsList)
        true
    }
    private lateinit var inputMethodManager: InputMethodManager

    companion object {
        const val TAG = "MainActivity"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        getNewList()
    }

    private fun initData() {
        inputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        binding.recyclerView.also {
            it.adapter = Adapter(
                { view, _, data ->
                    Log.d("TAG", "initData: $data")
                    view.apply {
                        tvTitle.text = data.title
                        tvTime.text = data.ctime
                        tvDesc.text = data.description
                        Glide.with(applicationContext)
                            .load(data.picUrl)
                            .into(ivPic)
                        cardView.setOnClickListener {
                            startActivity(Intent(this@MainActivity, WebActivity::class.java).apply {
                                putExtra("url", data.url)
                            })
                        }
                    }
                },
                { inflater, parent ->
                    ItemNewsBinding.inflate(inflater, parent, false)
                },
                newsList
            ).apply {
                adapter = this
            }
            it.layoutManager = LinearLayoutManager(applicationContext)
            it.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!it.canScrollVertically(1)) {
                        page++
                        getNewList()
                        Log.d(TAG, "滑动到底部: $page 页")
                    }
                }
            })
            it.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        closeSearch()
                    }
                }
            })
        }
        iv_search.setOnClickListener {
            showSearch()
        }
        iv_close.setOnClickListener {
            closeSearch()
        }
    }

    private fun showSearch() {
        if (!isSearch) {
            isSearch = true
            et_search.visibility = View.VISIBLE
            iv_close.visibility = View.VISIBLE
            et_search.hint = "请输入关键字"
            rl_search.layoutParams.apply {
                width = dip2px(px2dip(width) + 300)
            }
            rl_search.setPadding(14, 0, 14, 0)
            et_search.visibility = View.VISIBLE
            beginDelayedTransition()
        }
    }

    private fun closeSearch() {
        if (isSearch) {
            isSearch = false
            et_search.visibility = View.VISIBLE
            iv_close.visibility = View.GONE
            et_search.setText("")
            et_search.hint = ""
            rl_search.layoutParams.apply {
                width = dip2px(48)
                height = dip2px(48)
            }
            inputMethodManager.hideSoftInputFromWindow(window.decorView.windowToken, 0)
            beginDelayedTransition()
        }
    }

    private fun beginDelayedTransition() {
        autoTransition = AutoTransition()
        autoTransition.duration = 500
        TransitionManager.beginDelayedTransition(rl_search, autoTransition)
    }

    private fun getNewList() {
        val request = Request.Builder()
            .url("$BASE_URL$GET_NEWS&page=$page")
            .method("GET", null)
            .build()
        OkHttpClient().newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d(TAG, "NULL")
                }

                override fun onResponse(call: Call, response: Response) {
                    val news = Gson().fromJson(response.body()?.string(), News::class.java)
                    if (news != null) {
                        if (news.code == 200 && SUCCESS == news.msg) {
                            if (news.newslist != null) {
                                newsList = news.newslist
                                Log.d(TAG, "onResponse: ${newsList.size}")
                                val message = Message()
                                message.what = 1
                                handler.sendMessage(message)
                            }
                        }
                    }
                }
            })
    }

    // dp 转 px
    private fun dip2px(dpVale: Int): Int {
        val scale = resources.displayMetrics.density
        return (dpVale * scale + .5).toInt()
    }

    //px 转 dx
    private fun px2dip(pxValue: Int): Int {
        val scale = resources.displayMetrics.density
        return (pxValue / scale + .5).toInt()
    }
}