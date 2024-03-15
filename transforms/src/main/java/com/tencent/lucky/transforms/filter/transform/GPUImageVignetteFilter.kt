package com.tencent.lucky.transforms.filter.transform

import android.graphics.PointF
import android.opengl.GLES30
import com.tencent.lucky.transforms.filter.GPUImageFilter

/**
 * 暗角
 * Performs a vignetting effect, fading out the image at the edges
 * x, y: The directional intensity of the vignetting, with a default of x = 0.75, y = 0.5
 */
class GPUImageVignetteFilter (
    private var mVignetteCenter: PointF = PointF(0.5f,0.5f),
    private var mVignetteColor: FloatArray = floatArrayOf(0.0f, 0.0f, 0.0f),
    private var mVignetteStart: Float = 0.3f,
    private var mVignetteEnd: Float = 0.75f
) : GPUImageFilter(
    NO_FILTER_VERTEX_SHADER,
    VIGNETTING_FRAGMENT_SHADER
) {
    private var mVignetteCenterLocation = 0
    private var mVignetteColorLocation = 0
    private var mVignetteStartLocation = 0
    private var mVignetteEndLocation = 0

    override fun onInit() {
        super.onInit()
        mVignetteCenterLocation = GLES30.glGetUniformLocation(program, "vignetteCenter")
        mVignetteColorLocation = GLES30.glGetUniformLocation(program, "vignetteColor")
        mVignetteStartLocation = GLES30.glGetUniformLocation(program, "vignetteStart")
        mVignetteEndLocation = GLES30.glGetUniformLocation(program, "vignetteEnd")
        setVignetteCenter(mVignetteCenter)
        setVignetteColor(mVignetteColor)
        setVignetteStart(mVignetteStart)
        setVignetteEnd(mVignetteEnd)
    }

    fun setVignetteCenter(vignetteCenter: PointF) {
        mVignetteCenter = vignetteCenter
        setPoint(mVignetteCenterLocation, mVignetteCenter)
    }

    fun setVignetteColor(vignetteColor: FloatArray) {
        mVignetteColor = vignetteColor
        setFloatVec3(mVignetteColorLocation, mVignetteColor)
    }

    fun setVignetteStart(vignetteStart: Float) {
        mVignetteStart = vignetteStart
        setFloat(mVignetteStartLocation, mVignetteStart)
    }

    fun setVignetteEnd(vignetteEnd: Float) {
        mVignetteEnd = vignetteEnd
        setFloat(mVignetteEndLocation, mVignetteEnd)
    }

    companion object {
        const val VIGNETTING_FRAGMENT_SHADER = "" +
                " uniform sampler2D inputImageTexture;\n" +
                " varying highp vec2 textureCoordinate;\n" +
                " \n" +
                " uniform lowp vec2 vignetteCenter;\n" +
                " uniform lowp vec3 vignetteColor;\n" +
                " uniform highp float vignetteStart;\n" +
                " uniform highp float vignetteEnd;\n" +
                " \n" +
                " void main()\n" +
                " {\n" +
                "     /*\n" +
                "     lowp vec3 rgb = texture2D(inputImageTexture, textureCoordinate).rgb;\n" +
                "     lowp float d = distance(textureCoordinate, vec2(0.5,0.5));\n" +
                "     rgb *= (1.0 - smoothstep(vignetteStart, vignetteEnd, d));\n" +
                "     gl_FragColor = vec4(vec3(rgb),1.0);\n" +
                "      */\n" +
                "     \n" +
                "     lowp vec3 rgb = texture2D(inputImageTexture, textureCoordinate).rgb;\n" +
                "     lowp float d = distance(textureCoordinate, vec2(vignetteCenter.x, vignetteCenter.y));\n" +
                "     lowp float percent = smoothstep(vignetteStart, vignetteEnd, d);\n" +
                "     gl_FragColor = vec4(mix(rgb.x, vignetteColor.x, percent), mix(rgb.y, vignetteColor.y, percent), mix(rgb.z, vignetteColor.z, percent), 1.0);\n" +
                " }"
    }

}