package com.tencent.lucky.transforms.aekit

import com.tencent.aekit.api.standard.filter.AESmooth
import com.tencent.aekit.openrender.internal.Frame
import com.tencent.aekit.openrender.internal.FrameBufferCache
import com.tencent.lucky.transforms.base.BaseFilter
import com.tencent.lucky.transforms.util.AEUtil
import com.tencent.ttpic.openapi.filter.SpaceFilter

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: AEBeautyFilter
 * Author: kaelma
 * Date: 2020/4/28 5:22 PM
 * Description: aekit smooth filter (磨皮）
 * @param smoothLevel 0-100
 */
class AESmoothFilter(private var smoothLevel: Int = 55): BaseFilter {

    private var outputWidth = 0
    private var outputHeight = 0

    private lateinit var aeSmooth: AESmooth
    private lateinit var screenFilter: SpaceFilter

    override fun init() {
        aeSmooth = AESmooth()
        aeSmooth.apply()

        screenFilter = SpaceFilter()
        screenFilter.apply()
    }

    override fun onOutputSizeChanged(width: Int, height: Int) {
        outputWidth = width
        outputHeight = height
    }

    override fun draw(inputTexName: Int): Boolean {
        // face detect
        val faceAttr = AEUtil.detectFaceTexture(inputTexName, outputWidth, outputHeight)

        // smooth
        aeSmooth.setSmoothLevel(smoothLevel)
        aeSmooth.setSharpenSize(outputWidth, outputHeight)
        aeSmooth.setFaceAttr(faceAttr)
        val inputFrame = Frame(0, inputTexName, outputWidth, outputHeight)
        val smoothFrame = aeSmooth.render(inputFrame)

        // screen
        screenFilter.renderTexture(smoothFrame.textureId, smoothFrame.width, smoothFrame.height)

        // frameCache
        FrameBufferCache.getInstance().forceRecycle()

        return false
    }

    override fun destroy() {
        aeSmooth.clear()
        screenFilter.ClearGLSL()
    }
}