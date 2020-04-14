package com.tencent.lucky.transforms.fresco.aekit

import com.facebook.cache.common.CacheKey
import com.facebook.cache.common.SimpleCacheKey
import com.tencent.lucky.transforms.aekit.AEBeautyFilter
import com.tencent.lucky.transforms.aekit.BeautyType
import com.tencent.lucky.transforms.fresco.FrescoBaseTransform

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: AEBeautyFrescoTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description: 提供默认美颜参数
 */
class AEBeautyFrescoTransform(var type: BeautyType? = null, var level: Int = 0) :
    FrescoBaseTransform(AEBeautyFilter()){

    init {
        if (type != null) {
            (filter as AEBeautyFilter).setBeautyParam(type!!, level)
        }
    }

    override fun getPostprocessorCacheKey(): CacheKey? {
        return SimpleCacheKey("${name}.type=${type.hashCode()}.level=${level}")
    }
}