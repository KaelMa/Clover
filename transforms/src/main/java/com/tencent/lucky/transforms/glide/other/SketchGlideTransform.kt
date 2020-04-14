package com.tencent.lucky.transforms.glide.other

import com.tencent.lucky.transforms.filter.other.GPUImageSketchFilter
import com.tencent.lucky.transforms.glide.GlideBaseTransform

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: SketchGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description: look like a sketch
 */
class SketchGlideTransform : GlideBaseTransform(GPUImageSketchFilter()) {

    override fun equals(o: Any?): Boolean {
        return o is GPUImageSketchFilter
    }
}