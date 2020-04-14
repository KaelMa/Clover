package com.tencent.lucky.transforms.filter.base

import android.opengl.GLES30
import com.tencent.lucky.transforms.filter.GPUImageFilter

/**
 * gamma value ranges from 0.0 to 3.0, with 1.0 as the normal level
 */
class GPUImageGammaFilter(private var mGamma: Float = 1.2f)
    : GPUImageFilter(NO_FILTER_VERTEX_SHADER, GAMMA_FRAGMENT_SHADER) {

    private var mGammaLocation = 0

    override fun onInit() {
        super.onInit()
        mGammaLocation = GLES30.glGetUniformLocation(program, "gamma")
    }

    override fun onInitialized() {
        super.onInitialized()
        setGamma(mGamma)
    }

    fun setGamma(gamma: Float) {
        mGamma = gamma
        setFloat(mGammaLocation, mGamma)
    }

    companion object {
        const val GAMMA_FRAGMENT_SHADER = "" +
                "varying highp vec2 textureCoordinate;\n" +
                " \n" +
                " uniform sampler2D inputImageTexture;\n" +
                " uniform lowp float gamma;\n" +
                " \n" +
                " void main()\n" +
                " {\n" +
                "     lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
                "     \n" +
                "     gl_FragColor = vec4(pow(textureColor.rgb, vec3(gamma)), textureColor.w);\n" +
                " }"
    }

}