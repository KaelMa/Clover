package com.tencent.lucky.transforms.picasso.other

import com.tencent.lucky.transforms.filter.other.GPUImageAeroFilter
import com.tencent.lucky.transforms.picasso.PicassoBaseTransform

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: AeroGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description: 毛玻璃效果(白色蒙层 + 高斯模糊)
 */
class AeroPicassoTransform(
    var blurSize: Float = 2.0f,
    var opacity: Float = 0.36f,
    var blendColor: FloatArray = floatArrayOf(1.0f,1.0f,1.0f))
    : PicassoBaseTransform(GPUImageAeroFilter(blurSize, opacity, blendColor))
{
    override fun key(): String {
        return super.key() + ".blurSize=${blurSize}.opacity=${opacity}.blendColor=${blendColor}"
    }
}