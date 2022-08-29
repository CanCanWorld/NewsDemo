package com.zrq.newsdemo

import android.os.*
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.zrq.newsdemo.databinding.ActivityMainBinding
import com.zrq.newsdemo.utils.Constant.GET_ANIM_NEWS
import com.zrq.newsdemo.utils.Constant.GET_FILM_NEWS
import com.zrq.newsdemo.utils.Constant.GET_GAME_NEWS
import com.zrq.newsdemo.utils.Constant.GET_IT_NEWS
import com.zrq.newsdemo.utils.Constant.GET_PET_NEWS
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NewsFragment.OnRecyclerViewScroll {

    private lateinit var binding: ActivityMainBinding

    private var fragmentList = mutableListOf<NewsFragment>()
    private var tabList = mutableListOf<String>()
    private lateinit var autoTransition: AutoTransition
    private var word = ""
    private var isSearch = false
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
    }

    private fun initData() {
        inputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        fragmentList.apply {
            add(NewsFragment.newInstance(GET_GAME_NEWS)
                .apply { setOnRecyclerViewScroll(this@MainActivity) })
            add(NewsFragment.newInstance(GET_ANIM_NEWS)
                .apply { setOnRecyclerViewScroll(this@MainActivity) })
            add(NewsFragment.newInstance(GET_PET_NEWS)
                .apply { setOnRecyclerViewScroll(this@MainActivity) })
            add(NewsFragment.newInstance(GET_FILM_NEWS)
                .apply { setOnRecyclerViewScroll(this@MainActivity) })
            add(NewsFragment.newInstance(GET_IT_NEWS)
                .apply { setOnRecyclerViewScroll(this@MainActivity) })
        }
        tabList.apply {
            add("游戏")
            add("动漫")
            add("宠物")
            add("影视")
            add("IT")
        }
        binding.viewPager.apply {
            adapter = FragmentAdapter(supportFragmentManager, fragmentList, tabList)
            setOnScrollChangeListener { _, _, _, _, _ -> closeSearch() }
        }
        binding.tabLayout.setupWithViewPager(binding.viewPager)
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

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        if (event != null) {
            if (event.keyCode == KeyEvent.KEYCODE_ENTER) {
                inputMethodManager.hideSoftInputFromWindow(window.decorView.windowToken, 0)
                word = et_search.text.toString()
//                getNewList(GET_ANIM_NEWS)
            }
        }
        return super.dispatchKeyEvent(event)
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

    override fun scroll() {
        closeSearch()
    }
}