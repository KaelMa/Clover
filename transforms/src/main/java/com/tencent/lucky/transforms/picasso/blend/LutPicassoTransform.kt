package com.tencent.lucky.transforms.picasso.blend

import android.content.Context
import android.graphics.Bitmap
import com.tencent.lucky.transforms.base.BaseFilter
import com.tencent.lucky.transforms.filter.blend.GPUImageLookupFilter
import com.tencent.lucky.transforms.picasso.PicassoBaseTransform

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: BrightnessGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 */
class LutPicassoTransform private constructor(filter: BaseFilter)
    : PicassoBaseTransform(filter)
{
    private var intensity: Float = 1.0f
    private var bitmapHash: Int = 0

    /**
     * 次级构造方法
     * @param lutBitmap lut文件bitmap
     * @param intensity
     */
    constructor(lutBitmap: Bitmap,
                intensity: Float = 1.0f)
    : this(GPUImageLookupFilter(lutBitmap, intensity)) {
        this.intensity = intensity
        this.bitmapHash = lutBitmap.hashCode()
    }

    /**
     * 次级构造方法
     * @param lutPath lut文件的assets或者file路径
     * @param context
     * @param intensity
     */
    constructor(lutPath: String,
                context: Context,
                intensity: Float = 1.0f)
            :this(GPUImageLookupFilter(lutPath, context, intensity)) {
        this.intensity = intensity
        this.bitmapHash = lutPath.hashCode()
    }

    override fun key(): String {
        return super.key() + ".intensity=${intensity}.bitmapHash=${bitmapHash}"
    }
}