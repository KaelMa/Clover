package com.tencent.lucky.transforms.picasso.transform

import android.graphics.PointF
import com.tencent.lucky.transforms.filter.transform.GPUImageSwirlFilter
import com.tencent.lucky.transforms.picasso.PicassoBaseTransform

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: BrightnessGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 */
class SwirlPicassoTransform(private var radius: Float = 0.5f,
                            private var angle: Float = 1.0f,
                            private var center: PointF = PointF(0.5f, 0.5f)
): PicassoBaseTransform(GPUImageSwirlFilter(radius, angle, center))
{
    override fun key(): String {
        return super.key() + ".radius=${radius}.angle=${angle}.center=${center.hashCode()}"
    }
}