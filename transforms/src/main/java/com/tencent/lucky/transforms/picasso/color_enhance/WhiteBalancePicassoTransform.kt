package com.tencent.lucky.transforms.picasso.color_enhance

import com.tencent.lucky.transforms.filter.color_enhance.GPUImageWhiteBalanceFilter
import com.tencent.lucky.transforms.picasso.PicassoBaseTransform

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: BrightnessGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 */
class WhiteBalancePicassoTransform(private var temperature: Float = 5000.0f,
                                   private var tint: Float = 0.0f)
    : PicassoBaseTransform(GPUImageWhiteBalanceFilter(temperature, tint))
{
    override fun key(): String {
        return super.key() + ".temperature=${temperature}.tint=${tint}"
    }
}