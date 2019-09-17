package com.example.jsoupnext

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.*
import org.jsoup.Jsoup

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        launchUI({
//            val images = MyJsoupUtil.getImageFromBaidu("初音未来",1)
//            if(images!=null){
//                for (bean in images){
//                    Log.d("图片测试：",bean.thumbURL)
//                }
//            }


//            val images = MyJsoupUtil.getImageFromGoogle("初音未来")
//            if (images!=null)
//                for (image in images)
//                    Log.d("图片：",image)

            val images = ReptileImageUtil.getImageFromSogou("初音未来",2)
            if (images!=null)
                for (image in images)
                    Log.d("图片：",image.pic_url)
        },{
            Toast.makeText(this,"错误！",Toast.LENGTH_SHORT).show()
        })

    }

    private fun launchUI(block:suspend ()->Unit,error:suspend (error:Throws)->Unit) = runBlocking{
        try {
            CoroutineScope(Dispatchers.IO).launch { block() }
        }catch (e:Exception){
            error(e)
        }
    }
}

