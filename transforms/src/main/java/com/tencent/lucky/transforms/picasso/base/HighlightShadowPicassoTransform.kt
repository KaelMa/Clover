package com.tencent.lucky.transforms.picasso.base

import com.tencent.lucky.transforms.filter.base.GPUImageHighlightShadowFilter
import com.tencent.lucky.transforms.picasso.PicassoBaseTransform

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: BrightnessGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 */
class HighlightShadowPicassoTransform(private var shadows: Float = 0.0f,
                                      private var highlights: Float = 1.0f):
    PicassoBaseTransform(GPUImageHighlightShadowFilter(shadows, highlights))
{
    override fun key(): String {
        return super.key() + ".shadows=${shadows}.highlights=${highlights}"
    }
}