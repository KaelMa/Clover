package com.tencent.lucky.transforms.glide.aekit

import com.tencent.lucky.transforms.aekit.AEBeautyFilter
import com.tencent.lucky.transforms.aekit.BeautyType
import com.tencent.lucky.transforms.glide.GlideBaseTransform
import java.security.MessageDigest

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: AEBeautyGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description: 提供默认美颜参数
 */
class AEBeautyGlideTransform(var type: BeautyType? = null, var level: Int = 0) :
    GlideBaseTransform(AEBeautyFilter()){

    init {
        if (type != null) {
            (filter as AEBeautyFilter).setBeautyParam(type!!, level)
        }
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((id + type.hashCode() + level).toByteArray(CHARSET))
    }

    override fun equals(other: Any?): Boolean {
        return other is AEBeautyGlideTransform && other.type == type && other.level == level
    }

    override fun hashCode(): Int {
        return id.hashCode() + ((level + 1.0f) * 10).toInt() + if (type == null) 0 else type.hashCode()
    }
}