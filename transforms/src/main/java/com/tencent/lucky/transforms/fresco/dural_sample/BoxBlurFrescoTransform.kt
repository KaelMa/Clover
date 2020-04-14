package com.tencent.lucky.transforms.fresco.dural_sample

import com.facebook.cache.common.CacheKey
import com.facebook.cache.common.SimpleCacheKey
import com.tencent.lucky.transforms.filter.dural_sample.GPUImageBoxBlurFilter
import com.tencent.lucky.transforms.fresco.FrescoBaseTransform

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: BrightnessGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 */
class BoxBlurFrescoTransform(var blurSize: Float = 2.0f):
    FrescoBaseTransform(GPUImageBoxBlurFilter(blurSize))
{
    override fun getPostprocessorCacheKey(): CacheKey? {
        return SimpleCacheKey("${name}.blurSize=$blurSize")
    }
}