package com.tencent.lucky.transforms.picasso.dural_sample

import com.tencent.lucky.transforms.filter.dural_sample.GPUImageGaussianBlurFilter
import com.tencent.lucky.transforms.picasso.PicassoBaseTransform

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: BrightnessGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 */
class GaussianBlurPicassoTransform(var blurSize: Float = 2.0f):
    PicassoBaseTransform(GPUImageGaussianBlurFilter(blurSize))
{
    override fun key(): String {
        return super.key() + ".blurSize=$blurSize"
    }
}