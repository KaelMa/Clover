package com.tencent.lucky.transforms.filter.base

import android.opengl.GLES30
import com.tencent.lucky.transforms.filter.GPUImageFilter

/**
 * Blend texture with blend color and opacity
 * opacity: blend alpha
 * blendColor: blend color
 */
class GPUImageColorBlendFilter (
    private var mOpacity: Float = 0.36f,
    private var mBlendColor: FloatArray = floatArrayOf(1.0f,1.0f,1.0f))
    : GPUImageFilter(
        NO_FILTER_VERTEX_SHADER,
        COLOR_BLEND_FRAGMENT_SHADER
    ) {

    private var mOpacityLocation = 0
    private var mBlendColorPosition = 0

    override fun onInit() {
        super.onInit()
        mOpacityLocation = GLES30.glGetUniformLocation(program, "opacity")
        mBlendColorPosition = GLES30.glGetUniformLocation(program, "blendColor")
    }

    override fun onInitialized() {
        super.onInitialized()
        setOpacity(mOpacity)
        setBlendColor(mBlendColor)
    }

    fun setOpacity(opacity: Float) {
        mOpacity = opacity
        setFloat(mOpacityLocation, mOpacity)
    }

    fun setBlendColor(color: FloatArray) {
        mBlendColor = color
        setFloatVec3(mBlendColorPosition, mBlendColor)
    }

    companion object {
        const val COLOR_BLEND_FRAGMENT_SHADER = "" +
                "precision mediump float;\n" +
                "varying highp vec2 textureCoordinate;\n" +
                "uniform sampler2D inputImageTexture;\n" +
                "uniform float opacity;\n" +
                "uniform vec3 blendColor;\n" +
                "void main(){\n" +
                "    vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
                "    vec3 color = textureColor.rgb * (1.0 - opacity) + blendColor.rgb * opacity;\n" +
                "    gl_FragColor = vec4(color.rgb, 1.0);\n" +
                "}"
    }
}