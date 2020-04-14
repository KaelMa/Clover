package com.tencent.lucky.transforms.picasso.base

import com.tencent.lucky.transforms.filter.base.GPUImageExposureFilter
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
class ExposurePicassoTransform(var exposure: Float = 1.0f):
    PicassoBaseTransform(GPUImageExposureFilter(exposure))
{
    override fun key(): String {
        return super.key() + ".exposure=$exposure "
    }
}