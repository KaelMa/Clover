package com.tencent.lucky.transforms.filter.color_enhance

import android.opengl.GLES30
import com.tencent.lucky.transforms.filter.GPUImageFilter

/**
 * Bilateral, use to enhance image to make sure every pixels is weighted average whit surround.
 */
class GPUImageBilateralFilter(private var mDistanceNormalizationFactor: Float = 8.0f)
    : GPUImageFilter(
        BILATERAL_VERTEX_SHADER,
        BILATERAL_FRAGMENT_SHADER
    ) {

    private var mDisFactorLocation = 0
    private var mSingleStepOffsetLocation = 0

    override fun onInit() {
        super.onInit()
        mDisFactorLocation = GLES30.glGetUniformLocation(program, "distanceNormalizationFactor")
        mSingleStepOffsetLocation = GLES30.glGetUniformLocation(program, "singleStepOffset")
    }

    override fun onInitialized() {
        super.onInitialized()
        setDistanceNormalizationFactor(mDistanceNormalizationFactor)
    }

    override fun onOutputSizeChanged(width: Int, height: Int) {
        super.onOutputSizeChanged(width, height)
        setTexelSize(width.toFloat(), height.toFloat())
    }

    fun setDistanceNormalizationFactor(newValue: Float) {
        mDistanceNormalizationFactor = newValue
        setFloat(mDisFactorLocation, newValue)
    }

    private fun setTexelSize(w: Float, h: Float) {
        setFloatVec2(mSingleStepOffsetLocation, floatArrayOf(1.0f / w, 1.0f / h))
    }

    companion object {
        const val BILATERAL_VERTEX_SHADER = "" +
                "attribute vec4 position;\n" +
                "attribute vec4 inputTextureCoordinate;\n" +
                "const int GAUSSIAN_SAMPLES = 9;\n" +
                "uniform vec2 singleStepOffset;\n" +
                "varying vec2 textureCoordinate;\n" +
                "varying vec2 blurCoordinates[GAUSSIAN_SAMPLES];\n" +
                "void main()\n" +
                "{\n" +
                "	gl_Position = position;\n" +
                "	textureCoordinate = inputTextureCoordinate.xy;\n" +
                "	int multiplier = 0;\n" +
                "	vec2 blurStep;\n" +
                "	for (int i = 0; i < GAUSSIAN_SAMPLES; i++)\n" +
                "	{\n" +
                "		multiplier = (i - ((GAUSSIAN_SAMPLES - 1) / 2));\n" +
                "		blurStep = float(multiplier) * singleStepOffset;\n" +
                "		blurCoordinates[i] = inputTextureCoordinate.xy + blurStep;\n" +
                "	}\n" +
                "}"
        const val BILATERAL_FRAGMENT_SHADER = "" +
                "uniform sampler2D inputImageTexture;\n" +
                " const lowp int GAUSSIAN_SAMPLES = 9;\n" +
                " varying highp vec2 textureCoordinate;\n" +
                " varying highp vec2 blurCoordinates[GAUSSIAN_SAMPLES];\n" +
                " uniform mediump float distanceNormalizationFactor;\n" +
                " void main()\n" +
                " {\n" +
                "     lowp vec4 centralColor;\n" +
                "     lowp float gaussianWeightTotal;\n" +
                "     lowp vec4 sum;\n" +
                "     lowp vec4 sampleColor;\n" +
                "     lowp float distanceFromCentralColor;\n" +
                "     lowp float gaussianWeight;\n" +
                "     \n" +
                "     centralColor = texture2D(inputImageTexture, blurCoordinates[4]);\n" +
                "     gaussianWeightTotal = 0.18;\n" +
                "     sum = centralColor * 0.18;\n" +
                "     \n" +
                "     sampleColor = texture2D(inputImageTexture, blurCoordinates[0]);\n" +
                "     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n" +
                "     gaussianWeight = 0.05 * (1.0 - distanceFromCentralColor);\n" +
                "     gaussianWeightTotal += gaussianWeight;\n" +
                "     sum += sampleColor * gaussianWeight;\n" +
                "     sampleColor = texture2D(inputImageTexture, blurCoordinates[1]);\n" +
                "     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n" +
                "     gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor);\n" +
                "     gaussianWeightTotal += gaussianWeight;\n" +
                "     sum += sampleColor * gaussianWeight;\n" +
                "     sampleColor = texture2D(inputImageTexture, blurCoordinates[2]);\n" +
                "     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n" +
                "     gaussianWeight = 0.12 * (1.0 - distanceFromCentralColor);\n" +
                "     gaussianWeightTotal += gaussianWeight;\n" +
                "     sum += sampleColor * gaussianWeight;\n" +
                "     sampleColor = texture2D(inputImageTexture, blurCoordinates[3]);\n" +
                "     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n" +
                "     gaussianWeight = 0.15 * (1.0 - distanceFromCentralColor);\n" +
                "     gaussianWeightTotal += gaussianWeight;\n" +
                "     sum += sampleColor * gaussianWeight;\n" +
                "     sampleColor = texture2D(inputImageTexture, blurCoordinates[5]);\n" +
                "     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n" +
                "     gaussianWeight = 0.15 * (1.0 - distanceFromCentralColor);\n" +
                "     gaussianWeightTotal += gaussianWeight;\n" +
                "     sum += sampleColor * gaussianWeight;\n" +
                "     sampleColor = texture2D(inputImageTexture, blurCoordinates[6]);\n" +
                "     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n" +
                "     gaussianWeight = 0.12 * (1.0 - distanceFromCentralColor);\n" +
                "     gaussianWeightTotal += gaussianWeight;\n" +
                "     sum += sampleColor * gaussianWeight;\n" +
                "     sampleColor = texture2D(inputImageTexture, blurCoordinates[7]);\n" +
                "     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n" +
                "     gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor);\n" +
                "     gaussianWeightTotal += gaussianWeight;\n" +
                "     sum += sampleColor * gaussianWeight;\n" +
                "     sampleColor = texture2D(inputImageTexture, blurCoordinates[8]);\n" +
                "     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n" +
                "     gaussianWeight = 0.05 * (1.0 - distanceFromCentralColor);\n" +
                "     gaussianWeightTotal += gaussianWeight;\n" +
                "     sum += sampleColor * gaussianWeight;\n" +
                "     gl_FragColor = sum / gaussianWeightTotal;\n" +  //			" gl_FragColor.r = distanceNormalizationFactor / 20.0;" +
                " }"
    }

}