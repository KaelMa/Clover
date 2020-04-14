package com.tencent.lucky.transforms.fresco.aekit

import com.facebook.cache.common.CacheKey
import com.facebook.cache.common.SimpleCacheKey
import com.tencent.lucky.transforms.aekit.AESmoothFilter
import com.tencent.lucky.transforms.fresco.FrescoBaseTransform

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: AESmoothFrescoTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 */
class AESmoothFrescoTransform(var smoothLevel: Int = 55):
    FrescoBaseTransform(AESmoothFilter(smoothLevel))
{
    override fun getPostprocessorCacheKey(): CacheKey? {
        return SimpleCacheKey("${name}.smoothLevel=${smoothLevel}")
    }
}