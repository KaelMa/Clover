package com.tencent.lucky.transforms.filter.dural_sample

import android.opengl.GLES30
import com.tencent.lucky.transforms.filter.GPUImageFilter
import com.tencent.lucky.transforms.filter.GPUImageFilterGroup

/**
 * two-pass filter, whitch is always use in some filter that needs x and y two way to deal in a single task.
 */
open class GPUImageTwoPassTextureSamplingFilter(
    firstVertexShader: String?, firstFragmentShader: String?,
    secondVertexShader: String?, secondFragmentShader: String?
) : GPUImageFilterGroup(null) {

    init {
        addFilter(GPUImageFilter(firstVertexShader!!, firstFragmentShader!!))
        addFilter(GPUImageFilter(secondVertexShader!!, secondFragmentShader!!))
    }

    override fun onOutputSizeChanged(width: Int, height: Int) {
        super.onOutputSizeChanged(width, height)
        initTexelOffsets()
    }

    open fun initTexelOffsets() {
        var ratio = getHorizontalTexelOffsetRatio()
        var filter = filters!![0]
        var texelWidthOffsetLocation =
            GLES30.glGetUniformLocation(filter.program, "texelWidthOffset")
        var texelHeightOffsetLocation =
            GLES30.glGetUniformLocation(filter.program, "texelHeightOffset")
        filter.setFloat(texelWidthOffsetLocation, ratio / outputWidth)
        filter.setFloat(texelHeightOffsetLocation, 0f)
        ratio = getVerticalTexelOffsetRatio()
        filter = filters!![1]
        texelWidthOffsetLocation = GLES30.glGetUniformLocation(filter.program, "texelWidthOffset")
        texelHeightOffsetLocation = GLES30.glGetUniformLocation(filter.program, "texelHeightOffset")
        filter.setFloat(texelWidthOffsetLocation, 0f)
        filter.setFloat(texelHeightOffsetLocation, ratio / outputHeight)
    }

    open fun getVerticalTexelOffsetRatio(): Float {
        return 1f
    }

    open fun getHorizontalTexelOffsetRatio(): Float {
        return 1f
    }
}