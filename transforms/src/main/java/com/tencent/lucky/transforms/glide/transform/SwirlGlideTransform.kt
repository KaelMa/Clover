package com.tencent.lucky.transforms.glide.transform

import android.graphics.PointF
import com.tencent.lucky.transforms.filter.transform.GPUImageSwirlFilter
import com.tencent.lucky.transforms.glide.GlideBaseTransform
import java.security.MessageDigest

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: SwirlGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description: 漩涡，中间形成扭曲的画面
 */
class SwirlGlideTransform(private var radius: Float = 0.5f,
                          private var angle: Float = 1.0f,
                          private var center: PointF = PointF(0.5f, 0.5f)
): GlideBaseTransform(GPUImageSwirlFilter(radius, angle, center))
{
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((id + radius + angle + center).toByteArray(CHARSET))
    }

    override fun equals(o: Any?): Boolean {
        return o is SwirlGlideTransform && o.radius == radius && o.angle == angle && o.center == center
    }

    override fun hashCode(): Int {
        return id.hashCode() + ((radius + 1.0f) * 10).toInt() + ((angle + 1.0f) * 10).toInt() + center.hashCode()
    }
}