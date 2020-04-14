package com.tencent.lucky.transforms.filter.other

import com.tencent.lucky.transforms.filter.GPUImageFilterGroup
import com.tencent.lucky.transforms.filter.base.GPUImageColorBlendFilter
import com.tencent.lucky.transforms.filter.dural_sample.GPUImageGaussianBlurFilter

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: GPUAeroFilter
 * Author: kaelma
 * Date: 2020/10/21 11:55 AM
 * Description: 毛玻璃效果
 */
class GPUImageAeroFilter(
    blurSize: Float = 2f,
    opacity: Float = 0.36f,
    blendColor: FloatArray = floatArrayOf(1.0f,1.0f,1.0f))
    : GPUImageFilterGroup() {

    init {
        addFilter(GPUImageGaussianBlurFilter(blurSize))
        addFilter(GPUImageColorBlendFilter(opacity, blendColor))
    }
}