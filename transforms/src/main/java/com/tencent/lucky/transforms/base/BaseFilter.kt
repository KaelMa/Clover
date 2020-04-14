package com.tencent.lucky.transforms.base

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: BaseFilter
 * Author: kaelma
 * Date: 2020/4/13 7:17 PM
 * Description: 渲染filter
 */
interface BaseFilter {

    /**
     * init filter, must be called from gl thread
     */
    fun init()

    /**
     * on output size changed
     * @param width
     * @param height
     */
    fun onOutputSizeChanged(width: Int, height: Int) {}

    /**
     * draw
     * @param inputTexName input texture name
     * @return need flip
     */
    fun draw(inputTexName: Int): Boolean

    /**
     * destroy gl resource
     */
    fun destroy()
}