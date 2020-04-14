package com.tencent.lucky.transforms.filter.blend

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES30
import com.tencent.lucky.transforms.filter.GPUImageTwoInputFilter
import com.tencent.lucky.transforms.util.bitmapFromPath

/**
 * 颜色映射表渲染滤镜
 */
class GPUImageLookupFilter(): GPUImageTwoInputFilter(LOOKUP_FRAGMENT_SHADER) {

    private var mIntensityLocation: Int = 0

    private var mIntensity: Float = 1.0f

    /**
     * @param lutBitmap
     * @param intensity
     */
    constructor(lutBitmap: Bitmap,
                intensity: Float = 1.0f):this(){
        setLutBitmap(lutBitmap)
        setIntensity(intensity)
    }

    /**
     * @param lutPath decode bitmap from path, which can be file and assets.
     *                assets image must start with "assets://"
     * @param context
     * @param intensity
     */
    constructor(lutPath: String,
                context: Context,
                intensity: Float = 1.0f): this(){
        val lutBitmap = bitmapFromPath(context, lutPath)
        setLutBitmap(lutBitmap!!)
        setIntensity(intensity)
    }

    override fun onInit() {
        super.onInit()
        mIntensityLocation = GLES30.glGetUniformLocation(program, "intensity")
    }

    override fun onInitialized() {
        super.onInitialized()
        setIntensity(mIntensity)
    }

    fun setIntensity(intensity: Float) {
        mIntensity = intensity
        setFloat(mIntensityLocation, mIntensity)
    }

    fun setLutBitmap(lutBitmap: Bitmap) {
        setSecondInputBitmap(lutBitmap)
    }

    companion object {
        const val LOOKUP_FRAGMENT_SHADER = "" +
                " varying highp vec2 textureCoordinate;\n" +
                " varying highp vec2 textureCoordinate2;\n" +  // TODO: This is not used
                " \n" +
                " uniform sampler2D inputImageTexture;\n" +
                " uniform sampler2D inputImageTexture2;\n" +  // lookup texture
                " \n" +
                " uniform lowp float intensity;\n" +
                " \n" +
                " void main()\n" +
                " {\n" +
                "     highp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
                "     \n" +
                "     highp float blueColor = textureColor.b * 63.0;\n" +
                "     \n" +
                "     highp vec2 quad1;\n" +
                "     quad1.y = floor(floor(blueColor) / 8.0);\n" +
                "     quad1.x = floor(blueColor) - (quad1.y * 8.0);\n" +
                "     \n" +
                "     highp vec2 quad2;\n" +
                "     quad2.y = floor(ceil(blueColor) / 8.0);\n" +
                "     quad2.x = ceil(blueColor) - (quad2.y * 8.0);\n" +
                "     \n" +
                "     highp vec2 texPos1;\n" +
                "     texPos1.x = (quad1.x * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * textureColor.r);\n" +
                "     texPos1.y = (quad1.y * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * textureColor.g);\n" +
                "     \n" +
                "     highp vec2 texPos2;\n" +
                "     texPos2.x = (quad2.x * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * textureColor.r);\n" +
                "     texPos2.y = (quad2.y * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * textureColor.g);\n" +
                "     \n" +
                "     lowp vec4 newColor1 = texture2D(inputImageTexture2, texPos1);\n" +
                "     lowp vec4 newColor2 = texture2D(inputImageTexture2, texPos2);\n" +
                "     \n" +
                "     lowp vec4 newColor = mix(newColor1, newColor2, fract(blueColor));\n" +
                "     gl_FragColor = mix(textureColor, vec4(newColor.rgb, textureColor.w), intensity);\n" +
                " }"
    }

}