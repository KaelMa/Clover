package com.tencent.lucky.transforms.picasso.aekit

import com.tencent.lucky.transforms.aekit.AESmoothFilter
import com.tencent.lucky.transforms.picasso.PicassoBaseTransform

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: AESmoothPicassoTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 */
class AESmoothPicassoTransform(var smoothLevel: Int = 55):
    PicassoBaseTransform(AESmoothFilter(smoothLevel))
{
    override fun key(): String {
        return super.key() + ".smoothLevel=$smoothLevel"
    }
}