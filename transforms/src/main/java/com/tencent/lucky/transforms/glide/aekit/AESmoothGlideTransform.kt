package com.tencent.lucky.transforms.glide.aekit

import com.tencent.lucky.transforms.aekit.AESmoothFilter
import com.tencent.lucky.transforms.glide.GlideBaseTransform
import java.security.MessageDigest

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: AESmoothGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description: 磨皮
 */
class AESmoothGlideTransform(var smoothLevel: Int = 55):
    GlideBaseTransform(AESmoothFilter(smoothLevel))
{
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((id + smoothLevel).toByteArray(CHARSET))
    }

    override fun equals(o: Any?): Boolean {
        return o is AESmoothGlideTransform && o.smoothLevel == smoothLevel
    }

    override fun hashCode(): Int {
        return id.hashCode() + ((smoothLevel + 1.0f) * 10).toInt()
    }
}