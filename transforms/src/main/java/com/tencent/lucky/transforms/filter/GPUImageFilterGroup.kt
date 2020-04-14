package com.tencent.lucky.transforms.filter

import android.opengl.GLES30
import com.tencent.lucky.transforms.bean.Rotation
import com.tencent.lucky.transforms.util.TextureRotationUtil.TEXTURE_NO_ROTATION
import com.tencent.lucky.transforms.util.TextureRotationUtil.getRotationArray
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.util.*

/**
 *
 * Resembles a filter that consists of multiple filters applied after each
 * other.
 */
open class GPUImageFilterGroup (var mFilters: MutableList<GPUImageFilter>? = null) : GPUImageFilter() {

    private var mMergedFilters: MutableList<GPUImageFilter>? = null

    private var mFrameBuffers: IntArray? = null
    private var mFrameBufferTextures: IntArray? = null

    private val mGLCubeBuffer: FloatBuffer
    private val mGLTextureBuffer: FloatBuffer
    private val mGLTextureFlipBuffer: FloatBuffer

    /**
     * Gets the filters
     */
    val filters: List<GPUImageFilter>?
        get() = mFilters

    /** Gets the merged filters  */
    val mergedFilters: List<GPUImageFilter>?
        get() = mMergedFilters

    init {
        if (mFilters == null) {
            mFilters = ArrayList()
        } else {
            updateMergedFilters()
        }

        mGLCubeBuffer = ByteBuffer.allocateDirect(CUBE.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        mGLCubeBuffer.put(CUBE).position(0)

        mGLTextureBuffer = ByteBuffer.allocateDirect(TEXTURE_NO_ROTATION.size * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
        mGLTextureBuffer.put(TEXTURE_NO_ROTATION).position(0)

        val flipTexture = getRotationArray(Rotation.NORMAL, false, true)
        mGLTextureFlipBuffer = ByteBuffer.allocateDirect(flipTexture.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        mGLTextureFlipBuffer.put(flipTexture).position(0)
    }

    override fun onInit() {
        super.onInit()
        for (filter in mFilters!!) {
            filter.init()
        }
    }

    override fun onDestroy() {
        destroyFramebuffers()
        for (filter in mFilters!!) {
            filter.destroy()
        }
        super.onDestroy()
    }

    private fun destroyFramebuffers() {
        if (mFrameBufferTextures != null) {
            GLES30.glDeleteTextures(mFrameBufferTextures!!.size, mFrameBufferTextures, 0)
            mFrameBufferTextures = null
        }
        if (mFrameBuffers != null) {
            GLES30.glDeleteFramebuffers(mFrameBuffers!!.size, mFrameBuffers, 0)
            mFrameBuffers = null
        }
    }

    override fun onOutputSizeChanged(width: Int, height: Int) {
        super.onOutputSizeChanged(width, height)
        if (mFrameBuffers != null) {
            destroyFramebuffers()
        }
        var size = mFilters!!.size
        for (i in 0 until size) {
            mFilters!![i].onOutputSizeChanged(width, height)
        }

        if (mMergedFilters != null && mMergedFilters!!.size > 0) {
            size = mMergedFilters!!.size
            mFrameBuffers = IntArray(size - 1)
            mFrameBufferTextures = IntArray(size - 1)

            for (i in 0 until size - 1) {
                bindFbo(i, width, height)
            }
        }
    }

    private fun bindFbo(index: Int, width: Int, height: Int) {
        GLES30.glGenFramebuffers(1, mFrameBuffers, index)
        GLES30.glGenTextures(1, mFrameBufferTextures, index)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mFrameBufferTextures!![index])
        GLES30.glTexImage2D(
            GLES30.GL_TEXTURE_2D,
            0,
            GLES30.GL_RGBA, width, height,
            0,
            GLES30.GL_RGBA,
            GLES30.GL_UNSIGNED_BYTE,
            null
        )
        GLES30.glTexParameterf(
            GLES30.GL_TEXTURE_2D,
            GLES30.GL_TEXTURE_MAG_FILTER,
            GLES30.GL_LINEAR.toFloat()
        )
        GLES30.glTexParameterf(
            GLES30.GL_TEXTURE_2D,
            GLES30.GL_TEXTURE_MIN_FILTER,
            GLES30.GL_LINEAR.toFloat()
        )
        GLES30.glTexParameterf(
            GLES30.GL_TEXTURE_2D,
            GLES30.GL_TEXTURE_WRAP_S,
            GLES30.GL_CLAMP_TO_EDGE.toFloat()
        )
        GLES30.glTexParameterf(
            GLES30.GL_TEXTURE_2D,
            GLES30.GL_TEXTURE_WRAP_T,
            GLES30.GL_CLAMP_TO_EDGE.toFloat()
        )
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, mFrameBuffers!![index])
        GLES30.glFramebufferTexture2D(
            GLES30.GL_FRAMEBUFFER,
            GLES30.GL_COLOR_ATTACHMENT0,
            GLES30.GL_TEXTURE_2D,
            mFrameBufferTextures!![index], 0
        )
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0)
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0)
    }

    override fun onDraw(
        textureId: Int, cubeBuffer: FloatBuffer, textureBuffer: FloatBuffer
    ) {
        runPendingOnDrawTasks()
        if (!isInitialized || mFrameBuffers == null || mFrameBufferTextures == null) {
            return
        }
        if (mMergedFilters != null) {
            val size = mMergedFilters!!.size
            var previousTexture = textureId

            for (i in 0 until size) {
                val filter = mMergedFilters!![i]
                val isNotLast = i < size - 1
                if (isNotLast) {
                    GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, mFrameBuffers!![i])
                    GLES30.glClearColor(0f, 0f, 0f, 0f)
                }
                when (i) {
                    0 -> {
                        filter.onDraw(previousTexture, cubeBuffer, textureBuffer)
                    }
                    size - 1 -> {
                        filter.onDraw(previousTexture, mGLCubeBuffer,
                            if (size % 2 == 0) mGLTextureFlipBuffer else mGLTextureBuffer)
                    }
                    else -> {
                        filter.onDraw(previousTexture, mGLCubeBuffer, mGLTextureBuffer)
                    }
                }

                if (isNotLast) {
                    GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0)
                    if (mFrameBufferTextures == null) return
                    previousTexture = mFrameBufferTextures!![i]
                }
            }
        }
    }

    /**
     *  Add a filter to filter-list
     */
    fun addFilter(filter: GPUImageFilter?) {
        if (filter == null) {
            return
        }
        mFilters!!.add(filter)
        updateMergedFilters()
    }

    /**
     * Update merged filter-list
     */
    private fun updateMergedFilters() {
        if (mFilters == null) {
            return
        }
        if (mMergedFilters == null) {
            mMergedFilters = ArrayList()
        } else {
            mMergedFilters!!.clear()
        }

        var filters: List<GPUImageFilter>?
        for (filter in mFilters!!) {
            if (filter is GPUImageFilterGroup) {
                filter.updateMergedFilters()
                filters = filter.mergedFilters
                if (filters == null || filters.isEmpty()) continue
                mMergedFilters!!.addAll(filters)
                continue
            }
            mMergedFilters!!.add(filter)
        }
    }
}