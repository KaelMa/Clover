package com.tencent.lucky.transforms.fresco.color_enhance

import com.facebook.cache.common.CacheKey
import com.facebook.cache.common.SimpleCacheKey
import com.tencent.lucky.transforms.filter.color_enhance.GPUImageWhiteBalanceFilter
import com.tencent.lucky.transforms.fresco.FrescoBaseTransform

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: BrightnessGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 */
class WhiteBalanceFrescoTransform(private var temperature: Float = 5000.0f,
                                  private var tint: Float = 0.0f)
    : FrescoBaseTransform(GPUImageWhiteBalanceFilter(temperature, tint))
{
    override fun getPostprocessorCacheKey(): CacheKey? {
        return SimpleCacheKey("${name}.temperature=${temperature}.tint=${tint}")
    }
}