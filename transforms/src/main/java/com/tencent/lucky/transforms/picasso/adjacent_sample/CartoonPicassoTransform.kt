package com.tencent.lucky.transforms.picasso.adjacent_sample

import com.tencent.lucky.transforms.filter.adjacent_sample.GPUImageCartoonFilter
import com.tencent.lucky.transforms.picasso.PicassoBaseTransform

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: CartoonGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 */
class CartoonPicassoTransform(var threshold: Float = 0.2f,
                              var quantizationLevels: Float = 10.0f):
    PicassoBaseTransform(GPUImageCartoonFilter(threshold, quantizationLevels))
{
    override fun key(): String {
        return super.key() + ".threshold=${threshold}.quantizationLevels=${quantizationLevels}"
    }
}