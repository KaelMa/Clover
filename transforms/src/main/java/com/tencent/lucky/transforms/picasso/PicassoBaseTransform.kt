package com.tencent.lucky.transforms.picasso

import android.graphics.Bitmap
import com.squareup.picasso.Transformation
import com.tencent.lucky.transforms.base.BaseFilter
import com.tencent.lucky.transforms.base.Clover

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: GlideGPUTransform
 * Author: kaelma
 * Date: 2020/4/14 5:42 PM
 * Description: glide gpu transform
 */
open class PicassoBaseTransform(var filter: BaseFilter) : Transformation {

    val version = 1
    val id: String = "${javaClass.name}.${this.version}"

    override fun key(): String {
        return id
    }

    override fun transform(source: Bitmap): Bitmap {
        val outBitmap = Clover.with()
            .setImage(source)
            .setFilter(filter)
            .getFilterBitmap()

        return if (outBitmap != null) {
            source.recycle()
            outBitmap
        } else {
            source
        }
    }
}