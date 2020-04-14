package com.tencent.lucky.transforms.filter.base

import android.opengl.GLES30
import com.tencent.lucky.transforms.filter.GPUImageFilter

/**
 * saturation: The degree of saturation or desaturation to apply to the image (0.0 - 2.0, with 1.0 as the default)
 */
class GPUImageSaturationFilter(private var mSaturation: Float = 1.0f) :
    GPUImageFilter(
        NO_FILTER_VERTEX_SHADER,
        SATURATION_FRAGMENT_SHADER
    ) {
    private var mSaturationLocation = 0

    override fun onInit() {
        super.onInit()
        mSaturationLocation = GLES30.glGetUniformLocation(program, "saturation")
    }

    override fun onInitialized() {
        super.onInitialized()
        setSaturation(mSaturation)
    }

    fun setSaturation(saturation: Float) {
        mSaturation = saturation
        setFloat(mSaturationLocation, mSaturation)
    }

    companion object {
        const val SATURATION_FRAGMENT_SHADER = "" +
                " varying highp vec2 textureCoordinate;\n" +
                " \n" +
                " uniform sampler2D inputImageTexture;\n" +
                " uniform lowp float saturation;\n" +
                " \n" +
                " // Values from \"Graphics Shaders: Theory and Practice\" by Bailey and Cunningham\n" +
                " const mediump vec3 luminanceWeighting = vec3(0.2125, 0.7154, 0.0721);\n" +
                " \n" +
                " void main()\n" +
                " {\n" +
                "    lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
                "    lowp float luminance = dot(textureColor.rgb, luminanceWeighting);\n" +
                "    lowp vec3 greyScaleColor = vec3(luminance);\n" +
                "    \n" +
                "    gl_FragColor = vec4(mix(greyScaleColor, textureColor.rgb, saturation), textureColor.w);\n" +
                "     \n" +
                " }"
    }

}