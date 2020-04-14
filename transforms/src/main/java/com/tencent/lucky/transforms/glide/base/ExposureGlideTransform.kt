package com.tencent.lucky.transforms.glide.base

import com.tencent.lucky.transforms.filter.base.GPUImageExposureFilter
import com.tencent.lucky.transforms.glide.GlideBaseTransform
import java.security.MessageDigest

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: ExposureGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 * exposure: The adjusted exposure (-10.0 - 10.0, with 0.0 as the default)
 */
class ExposureGlideTransform(var exposure: Float = 1.0f):
    GlideBaseTransform(GPUImageExposureFilter(exposure))
{
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((id + exposure).toByteArray(CHARSET))
    }

    override fun equals(o: Any?): Boolean {
        return o is ExposureGlideTransform && o.exposure == exposure
    }

    override fun hashCode(): Int {
        return id.hashCode() + ((exposure + 1.0f) * 10).toInt()
    }
}