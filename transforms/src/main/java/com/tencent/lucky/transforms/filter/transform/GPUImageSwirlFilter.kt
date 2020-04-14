package com.tencent.lucky.transforms.filter.transform

import android.graphics.PointF
import android.opengl.GLES30
import com.tencent.lucky.transforms.filter.GPUImageFilter

/**
 * 漩涡，中间形成扭曲的画面
 * Creates a swirl distortion on the image.
 */
class GPUImageSwirlFilter (
    private var mRadius: Float = 0.5f,
    private var mAngle: Float = 1.0f,
    private var mCenter: PointF = PointF(0.5f, 0.5f)
) : GPUImageFilter(
    NO_FILTER_VERTEX_SHADER,
    SWIRL_FRAGMENT_SHADER
) {
    private var mAngleLocation = 0
    private var mRadiusLocation = 0
    private var mCenterLocation = 0
    override fun onInit() {
        super.onInit()
        mAngleLocation = GLES30.glGetUniformLocation(program, "angle")
        mRadiusLocation = GLES30.glGetUniformLocation(program, "radius")
        mCenterLocation = GLES30.glGetUniformLocation(program, "center")
    }

    override fun onInitialized() {
        super.onInitialized()
        setRadius(mRadius)
        setAngle(mAngle)
        setCenter(mCenter)
    }

    /**
     * The radius of the distortion, ranging from 0.0 to 1.0, with a default of 0.5.
     *
     * @param radius from 0.0 to 1.0, default 0.5
     */
    fun setRadius(radius: Float) {
        mRadius = radius
        setFloat(mRadiusLocation, radius)
    }

    /**
     * The amount of distortion to apply, with a minimum of 0.0 and a default of 1.0.
     *
     * @param angle minimum 0.0, default 1.0
     */
    fun setAngle(angle: Float) {
        mAngle = angle
        setFloat(mAngleLocation, angle)
    }

    /**
     * The center about which to apply the distortion, with a default of (0.5, 0.5).
     *
     * @param center default (0.5, 0.5)
     */
    fun setCenter(center: PointF) {
        mCenter = center
        setPoint(mCenterLocation, center)
    }

    companion object {
        const val SWIRL_FRAGMENT_SHADER = "" +
                "varying highp vec2 textureCoordinate;\n" +
                "\n" +
                "uniform sampler2D inputImageTexture;\n" +
                "\n" +
                "uniform highp vec2 center;\n" +
                "uniform highp float radius;\n" +
                "uniform highp float angle;\n" +
                "\n" +
                "void main()\n" +
                "{\n" +
                "highp vec2 textureCoordinateToUse = textureCoordinate;\n" +
                "highp float dist = distance(center, textureCoordinate);\n" +
                "if (dist < radius)\n" +
                "{\n" +
                "textureCoordinateToUse -= center;\n" +
                "highp float percent = (radius - dist) / radius;\n" +
                "highp float theta = percent * percent * angle * 8.0;\n" +
                "highp float s = sin(theta);\n" +
                "highp float c = cos(theta);\n" +
                "textureCoordinateToUse = vec2(dot(textureCoordinateToUse, vec2(c, -s)), dot(textureCoordinateToUse, vec2(s, c)));\n" +
                "textureCoordinateToUse += center;\n" +
                "}\n" +
                "\n" +
                "gl_FragColor = texture2D(inputImageTexture, textureCoordinateToUse );\n" +
                "\n" +
                "}\n"
    }

}