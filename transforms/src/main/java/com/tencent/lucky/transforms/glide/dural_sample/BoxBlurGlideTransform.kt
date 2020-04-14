package com.tencent.lucky.transforms.glide.dural_sample

import com.tencent.lucky.transforms.filter.dural_sample.GPUImageBoxBlurFilter
import com.tencent.lucky.transforms.glide.GlideBaseTransform
import java.security.MessageDigest

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: BoxBlurGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 * box blur effect
 */
class BoxBlurGlideTransform(var blurSize: Float = 2.0f):
    GlideBaseTransform(GPUImageBoxBlurFilter(blurSize))
{
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((id + blurSize).toByteArray(CHARSET))
    }

    override fun equals(o: Any?): Boolean {
        return o is BoxBlurGlideTransform && o.blurSize == blurSize
    }

    override fun hashCode(): Int {
        return id.hashCode() + ((blurSize + 1.0f) * 10).toInt()
    }
}