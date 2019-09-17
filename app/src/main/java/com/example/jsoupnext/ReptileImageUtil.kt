package com.example.jsoupnext

import android.util.Log
import com.google.gson.Gson
import org.jsoup.Jsoup

/**
 * create by 2019-9-16
 * @author lin
 * @param：word:搜索的关键字
 * @param：page:搜索的页码
 * @return 图片url地址字符串或对象或null
 */
object ReptileImageUtil {
    suspend fun getImageFromBaidu(word:String,page:Int = 1):ArrayList<BaiduImage>?{
        if (page<=0) return null
        val url =
            "https://image.baidu.com/search/acjson?tn=resultjson_com&ipn=rj&ct=201326592&fp=result&queryWord=$word&cl=2&lm=-1&ie=utf-8&oe=utf-8&st=-1&word=$word&face=0&istype=2&pn=${30*page}&rn=${30*page}"
        return try {
            val document = Jsoup.connect(url)
                .userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)")
                .timeout(3000)
                .get()
            val gson = Gson()
            val body = document.select("body")
            val baiduBean:BaiduImageBean = gson.fromJson(body.text(),BaiduImageBean::class.java)
            baiduBean.data
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getImageFromGoogle(word:String,page:Int = 1):ArrayList<String>?{
        if(page <= 0) return null
        val first = 35 * (page-1)
        val url =
            "https://cn.bing.com/images/async?q=${word}&first=${first}&count=35&relp=35&scenario=ImageBasicHover&datsrc=N_I&layout=RowBased_Landscape&mmasync=1&dgState=x*439_y*1206_h*188_c*3_i*71_r*15&IG=AFAE5519A70A4C6AB8C67220311AA0D6&SFX=3&iid=images.5633"
        return try {
            val document = Jsoup.connect(url)
                .userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)")
                .timeout(3000)
                .get()
            val images = ArrayList<String>()
            val uls = document
                .select("body")
                .select("div.dg_b")
                .select("div.dgControl")
            for (ul in uls){
                val lis = ul.select("li")
                for (li in lis){
                    val image = li
                        .select("div.iuscp")
                        .select("div.imgpt")
                        .select("a.iusc")
                        .select("div.img_cont")
                        .select("img.mimg")
                        if(image.size>0){
                            images.add(image[0].attr("src"))
                        }
                }
            }
            images
        }catch (e: Exception){
            null
        }
    }

    suspend fun getImageFromSogou(word:String,page:Int = 1):ArrayList<SogouImage>?{
        val start = 48 * if(page<=1) 1 else page
        val url =
            "https://pic.sogou.com/pics?query=$word&mode=1&start=$start&reqType=ajax&reqFrom=result&tn=0"
        return try {
            val document = Jsoup.connect(url)
                .userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)")
                .timeout(3000)
                .get()
            val body = document.select("body")
            val gson = Gson()
            val sougouImage = gson.fromJson(body.text(),SogouImageBean::class.java)
            sougouImage.items
        }catch (e:Exception){
            null
        }
    }
}