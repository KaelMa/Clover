package com.tencent.lucky.transforms.glide.color_enhance

import com.tencent.lucky.transforms.filter.color_enhance.GPUImageCGAColorspaceFilter
import com.tencent.lucky.transforms.glide.GlideBaseTransform

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: CGAColorspaceGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 * CGA色域着色效果，原有色泽被黑、浅蓝、紫色块替代，像素效果
 */
class CGAColorspaceGlideTransform : GlideBaseTransform(GPUImageCGAColorspaceFilter()) {
    override fun equals(o: Any?): Boolean {
        return o is CGAColorspaceGlideTransform
    }
}