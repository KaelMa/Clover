package com.tencent.lucky.transforms.fresco.base

import com.facebook.cache.common.CacheKey
import com.facebook.cache.common.SimpleCacheKey
import com.tencent.lucky.transforms.filter.base.GPUImageExposureFilter
import com.tencent.lucky.transforms.fresco.FrescoBaseTransform

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: BrightnessGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 */
class ExposureFrescoTransform(var exposure: Float = 1.0f):
    FrescoBaseTransform(GPUImageExposureFilter(exposure))
{
    override fun getPostprocessorCacheKey(): CacheKey? {
        return SimpleCacheKey("${name}.exposure=${exposure}")
    }
}