package com.tencent.lucky.transforms.picasso.transform

import android.graphics.PointF
import com.tencent.lucky.transforms.filter.transform.GPUImageGlassSphereFilter
import com.tencent.lucky.transforms.picasso.PicassoBaseTransform

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: BrightnessGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 */
class GlassSpherePicassoTransform(private var center: PointF = PointF(0.5f, 0.5f),
                                  private var radius: Float = 0.25f,
                                  private var refractiveIndex: Float = 0.71f
): PicassoBaseTransform(GPUImageGlassSphereFilter(center, radius, refractiveIndex))
{
    override fun key(): String {
        return super.key() + ".center=${center.hashCode()}.radius=${radius}.refractiveIndex=${refractiveIndex}"
    }
}