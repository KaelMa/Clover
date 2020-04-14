package com.tencent.lucky.transforms.glide.color_enhance

import com.tencent.lucky.transforms.filter.color_enhance.GPUImageColorMatrixFilter
import com.tencent.lucky.transforms.glide.GlideBaseTransform
import java.security.MessageDigest

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: ColorMatrixGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 * Applies a ColorMatrix to the image.
 */
class ColorMatrixGlideTransform(private var intensity: Float = 1.0f,
                                private var colorMatrix: FloatArray = floatArrayOf(
                                        1.0f, 0.0f, 0.0f, 0.0f,
                                        0.0f, 1.0f, 0.0f, 0.0f,
                                        0.0f, 0.0f, 1.0f, 0.0f,
                                        0.0f, 0.0f, 0.0f, 1.0f
                                ))
    : GlideBaseTransform(GPUImageColorMatrixFilter(intensity, colorMatrix))
{
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((id + intensity + colorMatrix).toByteArray(CHARSET))
    }

    override fun equals(o: Any?): Boolean {
        return o is ColorMatrixGlideTransform && o.intensity == intensity && o.colorMatrix.contentEquals(colorMatrix)
    }

    override fun hashCode(): Int {
        return id.hashCode() + ((intensity + 1.0f) * 10).toInt() + colorMatrix.hashCode()
    }
}