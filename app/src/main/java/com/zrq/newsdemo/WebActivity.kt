package com.zrq.newsdemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_web.*

class WebActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        loadWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadWebView() {
        val url = intent.getStringExtra("url")
        if (url == null) {
            Toast.makeText(applicationContext, "无法显示", Toast.LENGTH_SHORT).show()
            return
        }
        web_view.settings.apply {
            useWideViewPort = true
            loadWithOverviewMode = true
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
            allowFileAccess = true
            defaultTextEncodingName = "utf-8"
        }
        web_view.loadUrl(url)
    }
}