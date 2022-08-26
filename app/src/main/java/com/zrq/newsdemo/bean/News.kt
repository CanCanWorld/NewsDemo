package com.zrq.newsdemo.bean

/*
  "code": 200,
  "msg": "success",
  "newslist": [
    {
      "id": "b9325c9f2053872b4291966d9d45cb0e",
      "ctime": "2022-08-19 17:29",
      "title": "《海贼王剧场版红》乌塔新时代手办 S型好身材太棒",
      "description": "《海贼王》最新剧场版动画《ONEPIECEFILMRED》已经在日本上映，并且大受好评，官方也是趁热打铁推出了剧场版女主“UTA～新时代～”的手办。",
      "source": "动漫星空",
      "picUrl": "https://imgs.gamersky.com/upimg/new_preview/2022/08/19/origin_b_202208191727275184.jpg",
      "url": "https://acg.gamersky.com/otaku/zb/202208/1510613.shtml"
    }
 */
data class News(
    val msg: String = "",
    val code: Int = 0,
    val newslist: MutableList<NewslistItem>?
)

data class NewslistItem(
    val picUrl: String = "",
    val ctime: String = "",
    val description: String = "",
    val id: String = "",
    val source: String = "",
    val title: String = "",
    val url: String = ""
)