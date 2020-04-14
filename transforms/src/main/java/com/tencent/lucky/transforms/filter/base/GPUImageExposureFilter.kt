package com.tencent.lucky.transforms.filter.base

import android.opengl.GLES30
import com.tencent.lucky.transforms.filter.GPUImageFilter

/**
 * exposure: The adjusted exposure (-10.0 - 10.0, with 0.0 as the default)
 */
class GPUImageExposureFilter (private var mExposure: Float = 1.0f)
    : GPUImageFilter(NO_FILTER_VERTEX_SHADER, EXPOSURE_FRAGMENT_SHADER) {

    private var mExposureLocation = 0

    override fun onInit() {
        super.onInit()
        mExposureLocation = GLES30.glGetUniformLocation(program, "exposure")
    }

    override fun onInitialized() {
        super.onInitialized()
        setExposure(mExposure)
    }

    fun setExposure(exposure: Float) {
        mExposure = exposure
        setFloat(mExposureLocation, mExposure)
    }

    companion object {
        const val EXPOSURE_FRAGMENT_SHADER = "" +
                " varying highp vec2 textureCoordinate;\n" +
                " \n" +
                " uniform sampler2D inputImageTexture;\n" +
                " uniform highp float exposure;\n" +
                " \n" +
                " void main()\n" +
                " {\n" +
                "     highp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
                "     \n" +
                "     gl_FragColor = vec4(textureColor.rgb * pow(2.0, exposure), textureColor.w);\n" +
                " } "
    }

}