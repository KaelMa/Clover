package com.tencent.lucky.transforms.glide.color_enhance

import com.tencent.lucky.transforms.filter.color_enhance.GPUImageWhiteBalanceFilter
import com.tencent.lucky.transforms.glide.GlideBaseTransform
import java.security.MessageDigest

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: WhiteBalanceGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 * Adjusts the white balance of incoming image. To insure image adjust humans vision
 */
class WhiteBalanceGlideTransform(private var temperature: Float = 5000.0f,
                                 private var tint: Float = 0.0f)
    : GlideBaseTransform(GPUImageWhiteBalanceFilter(temperature, tint))
{
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((id + temperature + tint).toByteArray(CHARSET))
    }

    override fun equals(o: Any?): Boolean {
        return o is WhiteBalanceGlideTransform && o.temperature == temperature && o.tint == tint
    }

    override fun hashCode(): Int {
        return id.hashCode() + ((temperature + 1.0f) * 10).toInt() + ((tint + 1.0f) * 10).toInt()
    }
}