package com.tencent.lucky.transforms.filter.color_enhance

import android.opengl.GLES30
import com.tencent.lucky.transforms.filter.GPUImageFilter

/**
 * Applies a ColorMatrix to the image.
 */
class GPUImageColorMatrixFilter (
    private var mIntensity: Float = 1.0f,
    private var mColorMatrix: FloatArray = floatArrayOf(
        1.0f, 0.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f, 0.0f,
        0.0f, 0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 0.0f, 1.0f
    )
) : GPUImageFilter(
    NO_FILTER_VERTEX_SHADER,
    COLOR_MATRIX_FRAGMENT_SHADER
) {
    private var mColorMatrixLocation = 0
    private var mIntensityLocation = 0

    override fun onInit() {
        super.onInit()
        mColorMatrixLocation = GLES30.glGetUniformLocation(program, "colorMatrix")
        mIntensityLocation = GLES30.glGetUniformLocation(program, "intensity")
    }

    override fun onInitialized() {
        super.onInitialized()
        setIntensity(mIntensity)
        setColorMatrix(mColorMatrix)
    }

    fun setIntensity(intensity: Float) {
        mIntensity = intensity
        setFloat(mIntensityLocation, intensity)
    }

    fun setColorMatrix(colorMatrix: FloatArray) {
        mColorMatrix = colorMatrix
        setUniformMatrix4f(mColorMatrixLocation, colorMatrix)
    }

    companion object {
        const val COLOR_MATRIX_FRAGMENT_SHADER = "" +
                "varying highp vec2 textureCoordinate;\n" +
                "\n" +
                "uniform sampler2D inputImageTexture;\n" +
                "\n" +
                "uniform lowp mat4 colorMatrix;\n" +
                "uniform lowp float intensity;\n" +
                "\n" +
                "void main()\n" +
                "{\n" +
                "    lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
                "    lowp vec4 outputColor = textureColor * colorMatrix;\n" +
                "    \n" +
                "    gl_FragColor = (intensity * outputColor) + ((1.0 - intensity) * textureColor);\n" +
                "}"
    }

}