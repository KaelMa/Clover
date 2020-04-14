package com.tencent.lucky.transforms.filter.base

import com.tencent.lucky.transforms.filter.GPUImageFilter

/**
 * Applies a grayscale effect to the image.
 */
class GPUImageGrayscaleFilter : GPUImageFilter(NO_FILTER_VERTEX_SHADER, GRAYSCALE_FRAGMENT_SHADER) {

    companion object {
        const val GRAYSCALE_FRAGMENT_SHADER = "" +
                "precision highp float;\n" +
                "\n" +
                "varying vec2 textureCoordinate;\n" +
                "\n" +
                "uniform sampler2D inputImageTexture;\n" +
                "\n" +
                "const highp vec3 W = vec3(0.2125, 0.7154, 0.0721);\n" +
                "\n" +
                "void main()\n" +
                "{\n" +
                "  lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
                "  float luminance = dot(textureColor.rgb, W);\n" +
                "\n" +
                "  gl_FragColor = vec4(vec3(luminance), textureColor.a);\n" +
                "}"
    }

}