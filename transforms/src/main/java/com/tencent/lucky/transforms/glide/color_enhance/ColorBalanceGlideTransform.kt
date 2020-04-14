package com.tencent.lucky.transforms.glide.color_enhance

import com.tencent.lucky.transforms.filter.color_enhance.GPUImageColorBalanceFilter
import com.tencent.lucky.transforms.glide.GlideBaseTransform

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: ColorBalanceGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 * 色泽平衡滤镜，均匀色泽效果
 */
class ColorBalanceGlideTransform : GlideBaseTransform(GPUImageColorBalanceFilter()) {
    override fun equals(o: Any?): Boolean {
        return o is ColorBalanceGlideTransform
    }
}