package com.tencent.lucky.transforms.glide.transform

import android.graphics.PointF
import com.tencent.lucky.transforms.filter.transform.GPUImageGlassSphereFilter
import com.tencent.lucky.transforms.glide.GlideBaseTransform
import java.security.MessageDigest

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: GlassSphereGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description: 水晶球效果
 */
class GlassSphereGlideTransform(private var center: PointF = PointF(0.5f, 0.5f),
                                private var radius: Float = 0.25f,
                                private var refractiveIndex: Float = 0.71f
): GlideBaseTransform(GPUImageGlassSphereFilter(center, radius, refractiveIndex))
{
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((id + center + radius + refractiveIndex).toByteArray(CHARSET))
    }

    override fun equals(o: Any?): Boolean {
        return o is GlassSphereGlideTransform && o.radius == radius && o.refractiveIndex == refractiveIndex && o.center == center
    }

    override fun hashCode(): Int {
        return id.hashCode() + ((radius + 1.0f) * 10).toInt() + ((refractiveIndex + 1.0f) * 10).toInt() + center.hashCode()
    }
}