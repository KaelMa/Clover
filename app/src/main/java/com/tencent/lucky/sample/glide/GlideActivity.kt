package com.tencent.lucky.sample.glide

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tencent.lucky.sample.R
import com.tencent.lucky.sample.datas
import com.tencent.lucky.sample.gifUrls
import kotlinx.android.synthetic.main.activity_glide.*

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: GlideActivity
 * Author: kaelma
 * Date: 2020/4/15 4:12 PM
 * Description: GlideActivity
 */
class GlideActivity: AppCompatActivity() {

    companion object {
        const val IS_GIF = "is_gif"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glide)

        val isGif = intent.getBooleanExtra(IS_GIF, false)

        list.layoutManager = LinearLayoutManager(this)
        list.adapter = GlideAdapter(
            context = this,
            dataSet = datas,
            imageUrl = if (isGif) gifUrls[0] else R.drawable.avatar
        )
    }
    
}