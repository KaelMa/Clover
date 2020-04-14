package com.tencent.lucky.transforms.fresco.adjacent_sample

import com.facebook.cache.common.CacheKey
import com.facebook.cache.common.SimpleCacheKey
import com.tencent.lucky.transforms.filter.adjacent_sample.GPUImageCartoonFilter
import com.tencent.lucky.transforms.fresco.FrescoBaseTransform

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: CartoonGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 */
class CartoonFrescoTransform(var threshold: Float = 0.2f,
                             var quantizationLevels: Float = 10.0f):
    FrescoBaseTransform(GPUImageCartoonFilter(threshold, quantizationLevels))
{
    override fun getPostprocessorCacheKey(): CacheKey? {
        return SimpleCacheKey("${name}.threshold=${threshold}.quantizationLevels=${quantizationLevels}")
    }
}