package com.tencent.lucky.transforms.glide.other

import com.tencent.lucky.transforms.filter.other.GPUImageAeroFilter
import com.tencent.lucky.transforms.glide.GlideBaseTransform
import java.security.MessageDigest

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: AeroGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description: 毛玻璃效果(白色蒙层 + 高斯模糊)
 */
class AeroGlideTransform(
    var blurSize: Float = 2.0f,
    var opacity: Float = 0.36f,
    var blendColor: FloatArray = floatArrayOf(1.0f,1.0f,1.0f))
    : GlideBaseTransform(GPUImageAeroFilter(blurSize, opacity, blendColor))
{
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((id + blurSize + opacity + blendColor).toByteArray(CHARSET))
    }

    override fun equals(o: Any?): Boolean {
        return o is AeroGlideTransform && o.blurSize == blurSize && o.opacity == opacity && o.blendColor == blendColor
    }

    override fun hashCode(): Int {
        return id.hashCode() + ((blurSize + 1.0f) * 10).toInt() + ((opacity + 1.0f) * 10).toInt() + blendColor.hashCode()
    }
}