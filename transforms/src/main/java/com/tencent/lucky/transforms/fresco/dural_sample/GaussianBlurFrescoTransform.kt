package com.tencent.lucky.transforms.fresco.dural_sample

import com.facebook.cache.common.CacheKey
import com.facebook.cache.common.SimpleCacheKey
import com.tencent.lucky.transforms.filter.dural_sample.GPUImageGaussianBlurFilter
import com.tencent.lucky.transforms.fresco.FrescoBaseTransform

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: BrightnessGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 */
class GaussianBlurFrescoTransform(var blurSize: Float = 2.0f):
    FrescoBaseTransform(GPUImageGaussianBlurFilter(blurSize))
{
    override fun getPostprocessorCacheKey(): CacheKey? {
        return SimpleCacheKey("${name}.blurSize=$blurSize")
    }
}