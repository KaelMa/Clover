package com.tencent.lucky.transforms.filter.base

import android.opengl.GLES30
import com.tencent.lucky.transforms.filter.GPUImageFilter

/**
 * Adjusts the alpha channel of the incoming image
 * opacity: The value to multiply the incoming alpha channel for each pixel by (0.0 - 1.0, with 1.0 as the default)
 */
class GPUImageOpacityFilter (private var mOpacity: Float = 1.0f) :
    GPUImageFilter(
        NO_FILTER_VERTEX_SHADER,
        OPACITY_FRAGMENT_SHADER
    ) {

    private var mOpacityLocation = 0

    override fun onInit() {
        super.onInit()
        mOpacityLocation = GLES30.glGetUniformLocation(program, "opacity")
    }

    override fun onInitialized() {
        super.onInitialized()
        setOpacity(mOpacity)
    }

    fun setOpacity(opacity: Float) {
        mOpacity = opacity
        setFloat(mOpacityLocation, mOpacity)
    }

    companion object {
        const val OPACITY_FRAGMENT_SHADER = "" +
                "  varying highp vec2 textureCoordinate;\n" +
                "  \n" +
                "  uniform sampler2D inputImageTexture;\n" +
                "  uniform lowp float opacity;\n" +
                "  \n" +
                "  void main()\n" +
                "  {\n" +
                "      lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
                "      \n" +
                "      gl_FragColor = vec4(textureColor.rgb, textureColor.a * opacity);\n" +
                "  }\n"
    }

}