package com.tencent.lucky.transforms.fresco.other

import com.facebook.cache.common.CacheKey
import com.facebook.cache.common.SimpleCacheKey
import com.tencent.lucky.transforms.filter.other.GPUImageAeroFilter
import com.tencent.lucky.transforms.fresco.FrescoBaseTransform
import com.tencent.lucky.transforms.glide.GlideBaseTransform
import java.security.MessageDigest

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: AeroGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description: 毛玻璃效果(白色蒙层 + 高斯模糊)
 */
class AeroFrescoTransform(
    var blurSize: Float = 2.0f,
    var opacity: Float = 0.36f,
    var blendColor: FloatArray = floatArrayOf(1.0f,1.0f,1.0f))
    : FrescoBaseTransform(GPUImageAeroFilter(blurSize, opacity, blendColor))
{

    override fun getPostprocessorCacheKey(): CacheKey? {
        return SimpleCacheKey("${name}.blurSize=${blurSize}.opacity=${opacity}.blendColor=${blendColor}")
    }
}