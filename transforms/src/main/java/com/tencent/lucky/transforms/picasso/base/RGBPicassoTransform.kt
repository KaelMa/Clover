package com.tencent.lucky.transforms.picasso.base

import com.tencent.lucky.transforms.filter.base.GPUImageRGBFilter
import com.tencent.lucky.transforms.glide.GlideBaseTransform
import com.tencent.lucky.transforms.picasso.PicassoBaseTransform
import java.security.MessageDigest

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: BrightnessGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 */
class RGBPicassoTransform(private var red: Float = 1.0f,
                          private var green: Float = 1.0f,
                          private var blue: Float = 1.0f):
    PicassoBaseTransform(GPUImageRGBFilter(red, green, blue))
{
    override fun key(): String {
        return super.key() + ".red=${red}.green=${green}.blue=${blue}"
    }
}