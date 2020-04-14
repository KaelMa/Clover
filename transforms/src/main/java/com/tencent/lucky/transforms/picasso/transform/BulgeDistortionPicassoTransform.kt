package com.tencent.lucky.transforms.picasso.transform

import android.graphics.PointF
import com.tencent.lucky.transforms.filter.transform.GPUImageBulgeDistortionFilter
import com.tencent.lucky.transforms.picasso.PicassoBaseTransform

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: BrightnessGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 */
class BulgeDistortionPicassoTransform(private var radius: Float = 0.25f,
                                      private var scale: Float = 0.5f,
                                      private var center: PointF = PointF(0.5f, 0.5f)
): PicassoBaseTransform(GPUImageBulgeDistortionFilter(radius, scale, center))
{
    override fun key(): String {
        return super.key() + ".radius=${radius}.scale=${scale}.center=${center}"
    }
}