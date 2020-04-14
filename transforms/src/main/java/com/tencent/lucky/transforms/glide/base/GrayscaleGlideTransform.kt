package com.tencent.lucky.transforms.glide.base

import com.tencent.lucky.transforms.filter.base.GPUImageGrayscaleFilter
import com.tencent.lucky.transforms.glide.GlideBaseTransform

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: GrayscaleGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 * Applies a grayscale effect to the image
 */
class GrayscaleGlideTransform : GlideBaseTransform(GPUImageGrayscaleFilter()) {

    override fun equals(o: Any?): Boolean {
        return o is GrayscaleGlideTransform
    }
}