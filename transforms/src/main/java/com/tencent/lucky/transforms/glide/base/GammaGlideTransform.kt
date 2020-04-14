package com.tencent.lucky.transforms.glide.base

import com.tencent.lucky.transforms.filter.base.GPUImageGammaFilter
import com.tencent.lucky.transforms.glide.GlideBaseTransform
import java.security.MessageDigest

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: GammaGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 * gamma value ranges from 0.0 to 3.0, with 1.0 as the normal level
 */
class GammaGlideTransform(var gamma: Float = 1.2f):
    GlideBaseTransform(GPUImageGammaFilter(gamma))
{
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((id + gamma).toByteArray(CHARSET))
    }

    override fun equals(o: Any?): Boolean {
        return o is GammaGlideTransform && o.gamma == gamma
    }

    override fun hashCode(): Int {
        return id.hashCode() + ((gamma + 1.0f) * 10).toInt()
    }
}