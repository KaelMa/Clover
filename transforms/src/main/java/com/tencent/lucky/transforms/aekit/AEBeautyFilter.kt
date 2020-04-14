package com.tencent.lucky.transforms.aekit

import com.tencent.aekit.api.standard.filter.AEFaceBeauty
import com.tencent.aekit.api.standard.filter.AEFaceTransform
import com.tencent.aekit.api.standard.filter.AESmooth
import com.tencent.aekit.openrender.internal.Frame
import com.tencent.aekit.openrender.internal.FrameBufferCache
import com.tencent.lucky.transforms.base.BaseFilter
import com.tencent.lucky.transforms.util.AEUtil
import com.tencent.ttpic.openapi.config.BeautyRealConfig
import com.tencent.ttpic.openapi.filter.SpaceFilter

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: AEBeautyFilter
 * Author: kaelma
 * Date: 2020/4/28 5:22 PM
 * Description: aekit美颜滤镜，支持磨皮、美颜、形变
 *
 * 美颜功能支持参数如下
 *      TYPE.SMOOTH             磨皮
 *      TYPE.BEAUTY             美肤
 *      TYPE.EYE_LIGHTEN        亮眼
 *      TYPE.TOOTH_WHITEN       白牙
 *      TYPE.REMOVE_POUNCH      祛眼袋
 *      TYPE.REMOVE_WRINKLES    祛皱
 *      TYPE.REMOVE_WRINKLES2   祛法令纹
 *      TYPE.COLOR_TONE         肤色
 *      TYPE.CONTRAST_RATIO     对比度
 *
 * 支持的形变类型参考如下
 *      TYPE.FOREHEAD           调整发际线
 *      TYPE.EYE                大眼
 *      TYPE.EYE_DISTANCE       调整眼距
 *      TYPE.EYE_ANGLE          调整眼角
 *      TYPE.MOUTH_SHAPE        调整嘴型
 *      TYPE.CHIN               调整下巴
 *      TYPE.FACE_THIN          瘦脸
 *      TYPE.FACE_V             V脸
 *      TYPE.NOSE_WING          调整鼻翼
 *      TYPE.NOSE_POSITION      调整鼻子位置
 *      TYPE.LIPS_THICKNESS     调整嘴唇厚度
 *      TYPE.BASIC3             调整脸型(大眼瘦脸)
 *      TYPE.FACE_SHORTEN       短脸
 *      TYPE.NOSE               瘦鼻子
 */

class AEBeautyFilter: BaseFilter {

    private var outputWidth = 0
    private var outputHeight = 0

    private lateinit var aeSmooth: AESmooth
    private lateinit var aeFaceBeauty: AEFaceBeauty
    private lateinit var aeFaceTransform: AEFaceTransform
    private lateinit var screenFilter: SpaceFilter
    private var screenFrame = Frame()

    // --------- 美颜参数 --------- //
    private var smoothLevel // 磨皮参数
            = 55
    private var beautyNormalAlpha // 五官立体混合程度
            = 0
    private var beautyEyeLighten // 亮眼
            = 0
    private var beautyToothWhiten // 白牙
            = 0
    private var beautyRemovePounch // 祛眼袋
            = 0
    private var beautyRemoveWrinkles // 祛皱
            = 0
    private var beautyRemoveWrinkles2 // 祛法令纹
            = 0
    private var beautyColorTone // 肤色, 50代表没有效果
            = 50
    private var beautyContrastRatio // 对比度
            = 0
    private var transForehead // 发际线
            = 0
    private var transEye // 大眼
            = 0
    private var transEyeDistance // 眼距
            = 0
    private var transEyeAngle // 眼角
            = 0
    private var transMouthShape // 嘴形
            = 0
    private var transChin // 下巴
            = 0
    private var transFaceThin // 窄脸
            = 0
    private var transFaceV // V脸
            = 0
    private var transNoseWing // 鼻翼
            = 0
    private var transNosePosition // 鼻位置
            = 0
    private var transLipsThickness // 嘴唇厚度
            = 0
    private var transLipsWidth // 嘴唇宽度
            = 0
    private var transNose // 瘦鼻
            = 0
    private var transCheekboneThin // 瘦颧骨
            = 0
    private var transFaceShorten // 短脸
            = 0
    private var transBasic4 // 综合脸型
            = 0

    override fun init() {
        aeSmooth = AESmooth()
        aeSmooth.apply()

        aeFaceBeauty = AEFaceBeauty()
        aeFaceBeauty.apply()

        aeFaceTransform = AEFaceTransform()
        aeFaceTransform.apply()

        screenFilter = SpaceFilter()
        screenFilter.apply()
    }

    override fun onOutputSizeChanged(width: Int, height: Int) {
        outputWidth = width
        outputHeight = height
    }

    override fun draw(inputTexName: Int): Boolean {
        // set param
        configParams()

        // face detect
        val faceAttr = AEUtil.detectFaceTexture(inputTexName, outputWidth, outputHeight)

        // smooth
        aeSmooth.setSharpenSize(outputWidth, outputHeight)
        aeSmooth.setFaceAttr(faceAttr)
        val inputFrame = Frame(0, inputTexName, outputWidth, outputHeight)
        val smoothFrame = aeSmooth.render(inputFrame)

        // face beauty
        aeFaceBeauty.setVideoSize(outputWidth, outputHeight, AEUtil.DETECT_SCALE)
        aeFaceBeauty.setFaceAttr(faceAttr)
        val beautyFrame = aeFaceBeauty.render(smoothFrame)

        // face transform
        aeFaceTransform.setFaceStatus(faceAttr.allFacePoints, faceAttr.allFaceAngles,
            faceAttr.faceStatusList, AEUtil.DETECT_SCALE, 0f)
        val transformFrame = aeFaceTransform.render(beautyFrame)

        // screen
        screenFilter.renderTexture(transformFrame.textureId, transformFrame.width, transformFrame.height)

        // frameCache
        FrameBufferCache.getInstance().forceRecycle()

        return false
    }

    override fun destroy() {
        aeSmooth.clear()
        aeFaceBeauty.clear()
        aeFaceTransform.clear()

        screenFilter.ClearGLSL()
        screenFrame.clear()
    }

    private fun configParams() {
        aeSmooth.setSmoothLevel(smoothLevel)

        aeFaceBeauty.setNormalAlphaFactor((beautyNormalAlpha/100f)) // 五官立体混合程度
        aeFaceBeauty.setLipsLutAlpha(100) // 唇彩强度默认1.0f
        aeFaceBeauty.setFaceBeautyLevel(BeautyRealConfig.TYPE.EYE_LIGHTEN, beautyEyeLighten) // 亮眼
        aeFaceBeauty.setFaceBeautyLevel(BeautyRealConfig.TYPE.TOOTH_WHITEN, beautyToothWhiten) // 白牙
        aeFaceBeauty.setFaceBeautyLevel(BeautyRealConfig.TYPE.REMOVE_POUNCH, beautyRemovePounch) // 祛眼袋
        aeFaceBeauty.setFaceBeautyLevel(BeautyRealConfig.TYPE.REMOVE_WRINKLES, beautyRemoveWrinkles) // 祛皱
        aeFaceBeauty.setFaceBeautyLevel(BeautyRealConfig.TYPE.REMOVE_WRINKLES2, beautyRemoveWrinkles2) // 祛法令纹
        aeFaceBeauty.setFaceBeautyLevel(BeautyRealConfig.TYPE.COLOR_TONE, beautyColorTone) // 肤色
        aeFaceBeauty.setFaceBeautyLevel(BeautyRealConfig.TYPE.CONTRAST_RATIO, beautyContrastRatio) // 对比度

        aeFaceTransform.setFaceTransformLevel(BeautyRealConfig.TYPE.FOREHEAD, transForehead) // 发际线
        aeFaceTransform.setFaceTransformLevel(BeautyRealConfig.TYPE.EYE, transEye) // 大眼
        aeFaceTransform.setFaceTransformLevel(BeautyRealConfig.TYPE.EYE_DISTANCE, transEyeDistance) // 眼距
        aeFaceTransform.setFaceTransformLevel(BeautyRealConfig.TYPE.EYE_ANGLE, transEyeAngle) // 眼角
        aeFaceTransform.setFaceTransformLevel(BeautyRealConfig.TYPE.MOUTH_SHAPE, transMouthShape) // 嘴形
        aeFaceTransform.setFaceTransformLevel(BeautyRealConfig.TYPE.CHIN, transChin) // 下巴
        aeFaceTransform.setFaceTransformLevel(BeautyRealConfig.TYPE.FACE_THIN, transFaceThin) // 窄脸
        aeFaceTransform.setFaceTransformLevel(BeautyRealConfig.TYPE.FACE_V, transFaceV) // V脸
        aeFaceTransform.setFaceTransformLevel(BeautyRealConfig.TYPE.NOSE_WING, transNoseWing) // 鼻翼
        aeFaceTransform.setFaceTransformLevel(BeautyRealConfig.TYPE.NOSE_POSITION, transNosePosition) // 鼻位置
        aeFaceTransform.setFaceTransformLevel(BeautyRealConfig.TYPE.LIPS_THICKNESS, transLipsThickness) // 嘴唇厚度
        aeFaceTransform.setFaceTransformLevel(BeautyRealConfig.TYPE.LIPS_WIDTH, transLipsWidth) // 嘴唇宽度
        aeFaceTransform.setFaceTransformLevel(BeautyRealConfig.TYPE.NOSE, transNose) // 瘦鼻
        aeFaceTransform.setFaceTransformLevel(BeautyRealConfig.TYPE.CHEEKBONE_THIN, transCheekboneThin) // 瘦颧骨
        aeFaceTransform.setFaceTransformLevel(BeautyRealConfig.TYPE.FACE_SHORTEN, transFaceShorten) // 短脸
        aeFaceTransform.setFaceTransformLevel(BeautyRealConfig.TYPE.BASIC4, transBasic4) // 综合脸型
    }

    /**
     * 设置美颜参数
     * @param type [BeautyType]
     * @param level 0-100
     */
    fun setBeautyParam(type: BeautyType, level: Int) {
        when (type) {
            BeautyType.SmoothLevel -> smoothLevel = level
            BeautyType.BeautyNormalAlpha -> beautyNormalAlpha = level
            BeautyType.EyeLighten -> beautyEyeLighten = level
            BeautyType.ToothWhiten -> beautyToothWhiten = level
            BeautyType.RemovePounch -> beautyRemovePounch = level
            BeautyType.RemoveWrinkles -> beautyRemoveWrinkles = level
            BeautyType.RemoveWrinkles2 -> beautyRemoveWrinkles2 = level
            BeautyType.ColorTone -> beautyColorTone = level
            BeautyType.ContrastRatio -> beautyContrastRatio = level
            BeautyType.Forehead -> transForehead = level
            BeautyType.BeautyEye -> transEye = level
            BeautyType.EyeDistance -> transEyeDistance = level
            BeautyType.EyeAngle -> transEyeAngle = level
            BeautyType.MouthShape -> transMouthShape = level
            BeautyType.TransChin -> transChin = level
            BeautyType.FaceThin -> transFaceThin = level
            BeautyType.FaceV -> transFaceV = level
            BeautyType.NoseWing -> transNoseWing = level
            BeautyType.NosePosition -> transNosePosition = level
            BeautyType.LipsThickness -> transLipsThickness = level
            BeautyType.LipsWidth -> transLipsThickness = level
            BeautyType.NoseThin -> transNose = level
            BeautyType.CheekboneThin -> transCheekboneThin = level
            BeautyType.FaceShorten -> transFaceShorten = level
            BeautyType.TransBasic4 -> transBasic4 = level
        }
    }
}