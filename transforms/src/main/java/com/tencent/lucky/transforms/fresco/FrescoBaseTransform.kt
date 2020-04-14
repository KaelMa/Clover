package com.tencent.lucky.transforms.fresco

import android.graphics.Bitmap
import com.facebook.cache.common.CacheKey
import com.facebook.cache.common.SimpleCacheKey
import com.facebook.imagepipeline.request.BasePostprocessor
import com.tencent.lucky.transforms.base.BaseFilter
import com.tencent.lucky.transforms.base.Clover

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: FrescoBaseTransform
 * Author: kaelma
 * Date: 2020/4/23 2:28 PM
 * Description:
 */
open class FrescoBaseTransform(var filter: BaseFilter): BasePostprocessor() {

    override fun process(destBitmap: Bitmap, sourceBitmap: Bitmap) {
        val outBitmap = Clover.with()
            .setImage(sourceBitmap)
            .setFilter(filter)
            .getFilterBitmap()

        super.process(destBitmap, outBitmap)
    }

    override fun getName(): String {
        return javaClass.name
    }

    override fun getPostprocessorCacheKey(): CacheKey? {
        return SimpleCacheKey(name)
    }
}