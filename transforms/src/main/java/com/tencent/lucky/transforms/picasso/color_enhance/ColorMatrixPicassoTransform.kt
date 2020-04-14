package com.tencent.lucky.transforms.picasso.color_enhance

import com.tencent.lucky.transforms.filter.color_enhance.GPUImageColorMatrixFilter
import com.tencent.lucky.transforms.picasso.PicassoBaseTransform

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: BrightnessGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 */
class ColorMatrixPicassoTransform(private var intensity: Float = 1.0f,
                                  private var colorMatrix: FloatArray = floatArrayOf(
                                        1.0f, 0.0f, 0.0f, 0.0f,
                                        0.0f, 1.0f, 0.0f, 0.0f,
                                        0.0f, 0.0f, 1.0f, 0.0f,
                                        0.0f, 0.0f, 0.0f, 1.0f
                                ))
    : PicassoBaseTransform(GPUImageColorMatrixFilter(intensity, colorMatrix))
{
    //todo
    override fun key(): String {
        return super.key() + ".intensity=${intensity}.colorMatrix=${colorMatrix.hashCode()}"
    }
}