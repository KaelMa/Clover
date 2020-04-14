package com.tencent.lucky.transforms.filter.color_enhance

import com.tencent.lucky.transforms.filter.GPUImageFilter

/**
 * Invert all the colors in the image.
 */
class GPUImageColorInvertFilter
    : GPUImageFilter(NO_FILTER_VERTEX_SHADER, COLOR_INVERT_FRAGMENT_SHADER
) {

    companion object {
        const val COLOR_INVERT_FRAGMENT_SHADER = "" +
                "varying highp vec2 textureCoordinate;\n" +
                "\n" +
                "uniform sampler2D inputImageTexture;\n" +
                "\n" +
                "void main()\n" +
                "{\n" +
                "    lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
                "    \n" +
                "    gl_FragColor = vec4((1.0 - textureColor.rgb), textureColor.w);\n" +
                "}"
    }
}