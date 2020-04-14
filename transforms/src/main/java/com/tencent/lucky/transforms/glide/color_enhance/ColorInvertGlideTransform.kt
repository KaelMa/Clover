package com.tencent.lucky.transforms.glide.color_enhance

import com.tencent.lucky.transforms.filter.color_enhance.GPUImageColorInvertFilter
import com.tencent.lucky.transforms.glide.GlideBaseTransform

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: ColorInvertGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 * Invert all the colors in the image.
 */
class ColorInvertGlideTransform : GlideBaseTransform(GPUImageColorInvertFilter()) {
    override fun equals(o: Any?): Boolean {
        return o is ColorInvertGlideTransform
    }
}