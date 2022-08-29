package com.zrq.newsdemo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.zrq.newsdemo.bean.News
import com.zrq.newsdemo.bean.NewslistItem
import com.zrq.newsdemo.databinding.FragmentNewsBinding
import com.zrq.newsdemo.databinding.ItemNewsBinding
import com.zrq.newsdemo.utils.Constant
import okhttp3.*
import java.io.IOException

class NewsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentNewsBinding
    private var newsList = mutableListOf<NewslistItem>()
    private lateinit var adapter: Adapter<NewslistItem, ItemNewsBinding>
    private var page: Int = 1
    private var path = ""
    private var handler = Handler(Looper.myLooper()!!) { msg ->
        if (msg.what == 1) adapter.addNews(newsList)
        true
    }

    private lateinit var onRecyclerViewScroll: OnRecyclerViewScroll
    fun setOnRecyclerViewScroll(onRecyclerViewScroll: OnRecyclerViewScroll) {
        this.onRecyclerViewScroll = onRecyclerViewScroll
    }

    interface OnRecyclerViewScroll {
        fun scroll()
    }

    private fun initEvent() {
        binding.recyclerView.also {
            it.adapter = Adapter(
                { view, _, data ->
                    view.apply {
                        tvTitle.text = data.title
                        tvTime.text = data.ctime
                        tvDesc.text = data.description
                        Glide.with(requireActivity())
                            .load(data.picUrl)
                            .into(ivPic)
                        cardView.setOnClickListener {
                            startActivity(Intent(requireActivity(), WebActivity::class.java).apply {
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
            it.layoutManager = LinearLayoutManager(requireContext())
            it.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!it.canScrollVertically(1)) {
                        page++
                        getNewList(path)
                        Log.d(MainActivity.TAG, "滑动到底部: $page 页")
                    }
                }
            })
            it.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        onRecyclerViewScroll.scroll()
                    }
                }
            })
        }
    }

    private fun getNewList(path: String) {
        val url = "${Constant.BASE_URL}$path&page=$page"
        val request = Request.Builder()
            .url(url)
            .method("GET", null)
            .build()
        OkHttpClient().newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                }

                override fun onResponse(call: Call, response: Response) {
                    val news = Gson().fromJson(response.body()?.string(), News::class.java)
                    if (news != null) {
                        if (news.code == 200 && Constant.SUCCESS == news.msg) {
                            if (news.newslist != null) {
                                newsList = news.newslist
                                val message = Message()
                                message.what = 1
                                handler.sendMessage(message)
                            }
                        }
                    }
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(layoutInflater)
        initEvent()
        getNewList(path)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(path: String) =
            NewsFragment().apply {
                this.path = path
            }
    }
}