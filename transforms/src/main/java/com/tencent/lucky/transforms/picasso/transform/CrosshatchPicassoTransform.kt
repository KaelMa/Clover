package com.tencent.lucky.transforms.picasso.transform

import com.tencent.lucky.transforms.filter.transform.GPUImageCrosshatchFilter
import com.tencent.lucky.transforms.picasso.PicassoBaseTransform

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: BrightnessGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 */
class CrosshatchPicassoTransform(private var crossHatchSpacing: Float = 0.03f,
                                 private var lineWidth: Float = 0.003f
): PicassoBaseTransform(GPUImageCrosshatchFilter(crossHatchSpacing, lineWidth))
{
    override fun key(): String {
        return super.key() + ".crossHatchSpacing=${crossHatchSpacing}.lineWidth=${lineWidth}"
    }
}