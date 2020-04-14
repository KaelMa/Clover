package com.tencent.lucky.transforms.util

import com.tencent.aekit.openrender.internal.Frame
import com.tencent.ttpic.openapi.PTFaceAttr
import com.tencent.ttpic.openapi.PTFaceDetector

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: AEUtil
 * Author: kaelma
 * Date: 2020/4/28 5:44 PM
 * Description: aekit library util
 */
object AEUtil {

    const val DETECT_SCALE = 1.0/6.0

    /**
     * 检测人脸
     * @param texID 当前纹理ID
     * @param width 图片宽
     * @param height 图片高
     * @param faceDetectScale 人脸检测缩放系数
     */
    fun detectFaceTexture(texID: Int, width: Int, height: Int, faceDetectScale: Double = DETECT_SCALE): PTFaceAttr {
        val faceFrame = Frame(0, texID, width, height)
        val faceDetector = PTFaceDetector()
        faceDetector.init(true)
        val faceAttr = faceDetector.detectFrame(
            faceFrame,
            System.currentTimeMillis(),
            0,
            false,
            faceDetectScale,
            0f,
            true,
            null
        )
        faceFrame.clear()
        faceDetector.destroy()

        return faceAttr
    }

}