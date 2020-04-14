package com.tencent.lucky.transforms.glide.base

import com.tencent.lucky.transforms.filter.base.GPUImageRGBFilter
import com.tencent.lucky.transforms.glide.GlideBaseTransform
import java.security.MessageDigest

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: RGBGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 * Adjusts the individual RGB channels of an image
 * red,green,blue: Normalized values by which each color channel is multiplied. The range is from 0.0 up, with 1.0 as the default.
 */
class RGBGlideTransform( private var red: Float = 1.0f,
                         private var green: Float = 1.0f,
                         private var blue: Float = 1.0f):
    GlideBaseTransform(GPUImageRGBFilter(red, green, blue))
{
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((id + red + green + blue).toByteArray(CHARSET))
    }

    override fun equals(o: Any?): Boolean {
        return o is RGBGlideTransform && o.red == red && o.green == green && o.blue == blue
    }

    override fun hashCode(): Int {
        return id.hashCode() + ((red + 1.0f) * 10).toInt() + ((green + 1.0f) * 10).toInt() + ((blue + 1.0f) * 10).toInt()
    }
}