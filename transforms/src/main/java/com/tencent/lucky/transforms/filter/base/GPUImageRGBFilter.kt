package com.tencent.lucky.transforms.filter.base

import android.opengl.GLES30
import com.tencent.lucky.transforms.filter.GPUImageFilter

/**
 * Adjusts the individual RGB channels of an image
 * red,green,blue: Normalized values by which each color channel is multiplied. The range is from 0.0 up, with 1.0 as the default.
 *
 */
class GPUImageRGBFilter(
    private var mRed: Float = 1.0f,
    private var mGreen: Float = 1.0f,
    private var mBlue: Float = 1.0f
) : GPUImageFilter(
    NO_FILTER_VERTEX_SHADER,
    RGB_FRAGMENT_SHADER
) {
    private var mRedLocation = 0
    private var mGreenLocation = 0
    private var mBlueLocation = 0
    private var mIsInitialized = false

    override fun onInit() {
        super.onInit()
        mRedLocation = GLES30.glGetUniformLocation(program, "red")
        mGreenLocation = GLES30.glGetUniformLocation(program, "green")
        mBlueLocation = GLES30.glGetUniformLocation(program, "blue")
        mIsInitialized = true
        setRed(mRed)
        setGreen(mGreen)
        setBlue(mBlue)
    }

    fun setRed(red: Float) {
        mRed = red
        if (mIsInitialized) {
            setFloat(mRedLocation, mRed)
        }
    }

    fun setGreen(green: Float) {
        mGreen = green
        if (mIsInitialized) {
            setFloat(mGreenLocation, mGreen)
        }
    }

    fun setBlue(blue: Float) {
        mBlue = blue
        if (mIsInitialized) {
            setFloat(mBlueLocation, mBlue)
        }
    }

    companion object {
        const val RGB_FRAGMENT_SHADER = "" +
                "  varying highp vec2 textureCoordinate;\n" +
                "  \n" +
                "  uniform sampler2D inputImageTexture;\n" +
                "  uniform highp float red;\n" +
                "  uniform highp float green;\n" +
                "  uniform highp float blue;\n" +
                "  \n" +
                "  void main()\n" +
                "  {\n" +
                "      highp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
                "      \n" +
                "      gl_FragColor = vec4(textureColor.r * red, textureColor.g * green, textureColor.b * blue, 1.0);\n" +
                "  }\n"
    }

}