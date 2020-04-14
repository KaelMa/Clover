package com.tencent.lucky.transforms.base

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: Renderer
 * Author: kaelma
 * Date: 2020/4/13 6:36 PM
 * Description:
 */
interface Renderer {

    /**
     * init
     */
    fun onInit()

    /**
     * onSurfaceChanged
     * @param width
     * @param height
     */
    fun onSurfaceChanged(width: Int, height: Int)

    /**
     * draw frame
     * @return needFlip
     */
    fun onDrawFrame(): Boolean

    /**
     * release resource
     */
    fun onRelease()
}