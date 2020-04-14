package com.tencent.lucky.transforms.picasso.color_enhance

import com.tencent.lucky.transforms.filter.color_enhance.GPUImageBilateralFilter
import com.tencent.lucky.transforms.picasso.PicassoBaseTransform

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: BrightnessGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 */
class BilateralPicassoTransform(var distanceNormalizationFactor: Float = 8.0f):
    PicassoBaseTransform(GPUImageBilateralFilter(distanceNormalizationFactor))
{
    override fun key(): String {
        return super.key() + ".distanceNormalizationFactor=$distanceNormalizationFactor"
    }
}