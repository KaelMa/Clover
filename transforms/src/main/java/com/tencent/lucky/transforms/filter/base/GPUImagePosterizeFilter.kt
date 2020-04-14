package com.tencent.lucky.transforms.filter.base

import android.opengl.GLES30
import com.tencent.lucky.transforms.filter.GPUImageFilter

/**
 * Reduces the color range of the image.
 * colorLevels: ranges from 1 to 256, with a default of 10
 */
class GPUImagePosterizeFilter @JvmOverloads constructor(private var mColorLevels: Int = 10)
    : GPUImageFilter(
        NO_FILTER_VERTEX_SHADER,
        POSTERIZE_FRAGMENT_SHADER
    ) {

    private var mGLUniformColorLevels = 0

    override fun onInit() {
        super.onInit()
        mGLUniformColorLevels = GLES30.glGetUniformLocation(program, "colorLevels")
        setColorLevels(mColorLevels)
    }

    fun setColorLevels(colorLevels: Int) {
        mColorLevels = colorLevels
        setFloat(mGLUniformColorLevels, colorLevels.toFloat())
    }

    companion object {
        const val POSTERIZE_FRAGMENT_SHADER = "" +
                "varying highp vec2 textureCoordinate;\n" +
                "\n" +
                "uniform sampler2D inputImageTexture;\n" +
                "uniform highp float colorLevels;\n" +
                "\n" +
                "void main()\n" +
                "{\n" +
                "   highp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
                "   \n" +
                "   gl_FragColor = floor((textureColor * colorLevels) + vec4(0.5)) / colorLevels;\n" +
                "}"
    }

}