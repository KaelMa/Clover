package com.tencent.lucky.transforms.picasso.aekit

import com.tencent.lucky.transforms.aekit.AEBeautyFilter
import com.tencent.lucky.transforms.aekit.BeautyType
import com.tencent.lucky.transforms.picasso.PicassoBaseTransform

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: AEBeautyPicassoTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description: 提供默认美颜参数
 */
class AEBeautyPicassoTransform(var type: BeautyType? = null, var level: Int = 0) :
    PicassoBaseTransform(AEBeautyFilter()){

    init {
        if (type != null) {
            (filter as AEBeautyFilter).setBeautyParam(type!!, level)
        }
    }

    override fun key(): String {
        return super.key() + ".type=${type.hashCode()}.level=${level}"
    }
}