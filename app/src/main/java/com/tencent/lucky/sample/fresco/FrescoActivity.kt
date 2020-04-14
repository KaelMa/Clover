package com.tencent.lucky.sample.fresco

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.drawee.backends.pipeline.Fresco
import com.tencent.lucky.sample.R
import com.tencent.lucky.sample.datas
import kotlinx.android.synthetic.main.activity_glide.*

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: FrescoActivity
 * Author: kaelma
 * Date: 2020/4/15 4:12 PM
 * Description: FrescoActivity
 */
class FrescoActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this)
        setContentView(R.layout.activity_glide)

        list.layoutManager = LinearLayoutManager(this)
        list.adapter = FrescoAdapter(
            this,
            datas,
            R.drawable.dog
        )
    }
    
}