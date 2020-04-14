package com.tencent.lucky.transforms.fresco.color_enhance

import com.facebook.cache.common.CacheKey
import com.facebook.cache.common.SimpleCacheKey
import com.tencent.lucky.transforms.filter.color_enhance.GPUImageColorMatrixFilter
import com.tencent.lucky.transforms.fresco.FrescoBaseTransform

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: BrightnessGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 */
class ColorMatrixFrescoTransform(private var intensity: Float = 1.0f,
                                 private var colorMatrix: FloatArray = floatArrayOf(
                                        1.0f, 0.0f, 0.0f, 0.0f,
                                        0.0f, 1.0f, 0.0f, 0.0f,
                                        0.0f, 0.0f, 1.0f, 0.0f,
                                        0.0f, 0.0f, 0.0f, 1.0f
                                ))
    : FrescoBaseTransform(GPUImageColorMatrixFilter(intensity, colorMatrix))
{

    override fun getPostprocessorCacheKey(): CacheKey? {
        return SimpleCacheKey("${name}.intensity=${intensity}.colorMatrix=${colorMatrix.hashCode()}")
    }
}