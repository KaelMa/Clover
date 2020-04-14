package com.tencent.lucky.transforms.picasso.transform

import android.graphics.PointF
import com.tencent.lucky.transforms.filter.transform.GPUImageVignetteFilter
import com.tencent.lucky.transforms.picasso.PicassoBaseTransform

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: BrightnessGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 */
class VignettePicassoTransform(private var center: PointF = PointF(0.5f,0.5f),
                               private var color: FloatArray = floatArrayOf(0.0f, 0.0f, 0.0f),
                               private var start: Float = 0.3f,
                               private var end: Float = 0.75f)
: PicassoBaseTransform(GPUImageVignetteFilter(center, color, start, end))
{
    override fun key(): String {
        return super.key() + ".center=${center.hashCode()}.color=${color.hashCode()}.start=${start}.end=${end}"
    }
}