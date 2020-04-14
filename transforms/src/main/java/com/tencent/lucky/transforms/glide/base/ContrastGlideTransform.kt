package com.tencent.lucky.transforms.glide.base

import com.tencent.lucky.transforms.filter.base.GPUImageContrastFilter
import com.tencent.lucky.transforms.glide.GlideBaseTransform
import java.security.MessageDigest

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: ContrastGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 * Changes the contrast of the image.
 * contrast value ranges from 0.0 to 4.0, with 1.0 as the normal level
 */
class ContrastGlideTransform(var contrast: Float = 1.2f):
    GlideBaseTransform(GPUImageContrastFilter(contrast))
{
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((id + contrast).toByteArray(CHARSET))
    }

    override fun equals(o: Any?): Boolean {
        return o is ContrastGlideTransform && o.contrast == contrast
    }

    override fun hashCode(): Int {
        return id.hashCode() + ((contrast + 1.0f) * 10).toInt()
    }
}