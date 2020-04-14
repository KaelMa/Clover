package com.tencent.lucky.transforms.filter.base

import android.opengl.GLES30
import com.tencent.lucky.transforms.filter.GPUImageFilter

/**
 * 色阶滤镜
 */
class GPUImageLevelsFilter private constructor(
    private val mMin: FloatArray,
    private val mMid: FloatArray,
    private val mMax: FloatArray,
    private val mMinOutput: FloatArray,
    private val mMaxOutput: FloatArray
) : GPUImageFilter(
    NO_FILTER_VERTEX_SHADER,
    LEVELS_FRAGMET_SHADER
) {
    companion object {
        const val LEVELS_FRAGMET_SHADER = "" +
                " varying highp vec2 textureCoordinate;\n" +
                " \n" +
                " uniform sampler2D inputImageTexture;\n" +
                " uniform mediump vec3 levelMinimum;\n" +
                " uniform mediump vec3 levelMiddle;\n" +
                " uniform mediump vec3 levelMaximum;\n" +
                " uniform mediump vec3 minOutput;\n" +
                " uniform mediump vec3 maxOutput;\n" +
                " \n" +
                " void main()\n" +
                " {\n" +
                "     mediump vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
                "     \n" +
                "     gl_FragColor = vec4( mix(minOutput, maxOutput, pow(min(max(textureColor.rgb -levelMinimum, vec3(0.0)) / (levelMaximum - levelMinimum  ), vec3(1.0)), 1.0 /levelMiddle)) , textureColor.a);\n" +
                " }\n"
    }

    private var mMinLocation = 0
    private var mMidLocation = 0
    private var mMaxLocation = 0
    private var mMinOutputLocation = 0
    private var mMaxOutputLocation = 0

    constructor() : this(
        floatArrayOf(0.0f, 0.0f, 0.0f),
        floatArrayOf(1.0f, 1.0f, 1.0f),
        floatArrayOf(1.0f, 1.0f, 1.0f),
        floatArrayOf(0.0f, 0.0f, 0.0f),
        floatArrayOf(1.0f, 1.0f, 1.0f)
    )

    init {
        setMin(0.0f, 1.0f, 1.0f, 0.0f, 1.0f)
    }

    override fun onInit() {
        super.onInit()
        mMinLocation = GLES30.glGetUniformLocation(program, "levelMinimum")
        mMidLocation = GLES30.glGetUniformLocation(program, "levelMiddle")
        mMaxLocation = GLES30.glGetUniformLocation(program, "levelMaximum")
        mMinOutputLocation = GLES30.glGetUniformLocation(program, "minOutput")
        mMaxOutputLocation = GLES30.glGetUniformLocation(program, "maxOutput")
    }

    override fun onInitialized() {
        super.onInitialized()
        updateUniforms()
    }

    fun updateUniforms() {
        setFloatVec3(mMinLocation, mMin)
        setFloatVec3(mMidLocation, mMid)
        setFloatVec3(mMaxLocation, mMax)
        setFloatVec3(mMinOutputLocation, mMinOutput)
        setFloatVec3(mMaxOutputLocation, mMaxOutput)
    }

    fun setMin(
        min: Float,
        mid: Float,
        max: Float,
        minOut: Float,
        maxOut: Float
    ) {
        setRedMin(min, mid, max, minOut, maxOut)
        setGreenMin(min, mid, max, minOut, maxOut)
        setBlueMin(min, mid, max, minOut, maxOut)
    }

    fun setMin(min: Float, mid: Float, max: Float) {
        setMin(min, mid, max, 0.0f, 1.0f)
    }

    fun setRedMin(
        min: Float,
        mid: Float,
        max: Float,
        minOut: Float,
        maxOut: Float
    ) {
        mMin[0] = min
        mMid[0] = mid
        mMax[0] = max
        mMinOutput[0] = minOut
        mMaxOutput[0] = maxOut
        updateUniforms()
    }

    fun setRedMin(min: Float, mid: Float, max: Float) {
        setRedMin(min, mid, max, 0f, 1f)
    }

    fun setGreenMin(
        min: Float,
        mid: Float,
        max: Float,
        minOut: Float,
        maxOut: Float
    ) {
        mMin[1] = min
        mMid[1] = mid
        mMax[1] = max
        mMinOutput[1] = minOut
        mMaxOutput[1] = maxOut
        updateUniforms()
    }

    fun setGreenMin(min: Float, mid: Float, max: Float) {
        setGreenMin(min, mid, max, 0f, 1f)
    }

    fun setBlueMin(
        min: Float,
        mid: Float,
        max: Float,
        minOut: Float,
        maxOut: Float
    ) {
        mMin[2] = min
        mMid[2] = mid
        mMax[2] = max
        mMinOutput[2] = minOut
        mMaxOutput[2] = maxOut
        updateUniforms()
    }

    fun setBlueMin(min: Float, mid: Float, max: Float) {
        setBlueMin(min, mid, max, 0f, 1f)
    }
}