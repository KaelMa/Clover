package com.tencent.lucky.transforms.picasso.base

import com.tencent.lucky.transforms.filter.base.GPUImageContrastFilter
import com.tencent.lucky.transforms.picasso.PicassoBaseTransform

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: BrightnessGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 */
class ContrastPicassoTransform(var contrast: Float = 1.2f):
    PicassoBaseTransform(GPUImageContrastFilter(contrast))
{
    override fun key(): String {
        return super.key() + ".contrast=$contrast"
    }
}