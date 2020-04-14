package com.tencent.lucky.transforms.glide.transform

import android.graphics.PointF
import com.tencent.lucky.transforms.filter.transform.GPUImageBulgeDistortionFilter
import com.tencent.lucky.transforms.glide.GlideBaseTransform
import java.security.MessageDigest

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: BulgeDistortionGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 * this conversion filter provide effect, which looks like fish eyes.
 */
class BulgeDistortionGlideTransform(private var radius: Float = 0.25f,
                                    private var scale: Float = 0.5f,
                                    private var center: PointF = PointF(0.5f, 0.5f)
): GlideBaseTransform(GPUImageBulgeDistortionFilter(radius, scale, center))
{
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((id + radius + scale + center).toByteArray(CHARSET))
    }

    override fun equals(o: Any?): Boolean {
        return o is BulgeDistortionGlideTransform && o.radius == radius && o.scale == scale && o.center == center
    }

    override fun hashCode(): Int {
        return id.hashCode() + ((radius + 1.0f) * 10).toInt() + ((scale + 1.0f) * 10).toInt() + center.hashCode()
    }
}