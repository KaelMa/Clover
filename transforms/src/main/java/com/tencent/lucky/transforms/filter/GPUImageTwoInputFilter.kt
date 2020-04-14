package com.tencent.lucky.transforms.filter

import android.graphics.Bitmap
import android.opengl.GLES30
import com.tencent.lucky.transforms.bean.Rotation
import com.tencent.lucky.transforms.util.GLUtil.createTexture
import com.tencent.lucky.transforms.util.TextureRotationUtil.getRotationArray
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * deal with 2 input texture, you should prepare your fragment-shader.
 */
open class GPUImageTwoInputFilter(vertexShader: String, fragmentShader: String)
    : GPUImageFilter(vertexShader, fragmentShader) {

    private var mFilterSecondTextureCoordinateAttribute = 0
    private var mFilterInputTextureUniform2 = 0
    private var mFilterSourceTexture2 = DEFAULT_NO_IMAGE
    private var mTexture2CoordinatesBuffer: ByteBuffer? = null
    private var mSecondBitmap: Bitmap? = null

    constructor(fragmentShader: String) : this(VERTEX_SHADER, fragmentShader)

    init {
        setRotation(Rotation.NORMAL, flipHorizontal = false, flipVertical = false)
    }

    override fun onInit() {
        super.onInit()
        mFilterSecondTextureCoordinateAttribute = GLES30.glGetAttribLocation(program, "inputTextureCoordinate2")
        mFilterInputTextureUniform2 = GLES30.glGetUniformLocation(program, "inputImageTexture2")

        if (mSecondBitmap != null && !mSecondBitmap!!.isRecycled) {
            setSecondInputBitmap(mSecondBitmap!!)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        GLES30.glDeleteTextures(1, intArrayOf(mFilterSourceTexture2), 0)
        mFilterSourceTexture2 = DEFAULT_NO_IMAGE
    }

    override fun onDrawArraysPre() {
        GLES30.glActiveTexture(GLES30.GL_TEXTURE3)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mFilterSourceTexture2)
        GLES30.glUniform1i(mFilterInputTextureUniform2, 3)

        // 部分编译器对于未用的attribute，会自动优化掉
        if (mFilterSecondTextureCoordinateAttribute > 0) {
            mTexture2CoordinatesBuffer!!.position(0)
            GLES30.glVertexAttribPointer(
                mFilterSecondTextureCoordinateAttribute,
                2,
                GLES30.GL_FLOAT,
                false,
                0,
                mTexture2CoordinatesBuffer
            )
            GLES30.glEnableVertexAttribArray(mFilterSecondTextureCoordinateAttribute)
        }
    }

    fun getSecondInputBitmap(): Bitmap? {
        return mSecondBitmap
    }

    fun setSecondInputBitmap(bitmap: Bitmap) {
        if (bitmap.isRecycled) {
            return
        }
        mSecondBitmap = bitmap
        runOnDraw(Runnable {
            if (mFilterSourceTexture2 == DEFAULT_NO_IMAGE) {
                if (mSecondBitmap == null || mSecondBitmap!!.isRecycled) {
                    return@Runnable
                }
                GLES30.glActiveTexture(GLES30.GL_TEXTURE3)
                mFilterSourceTexture2 = createTexture(
                    mSecondBitmap!!,
                    DEFAULT_NO_IMAGE,
                    false
                )
            }
        })
    }

    private fun setRotation(rotation: Rotation, flipHorizontal: Boolean, flipVertical: Boolean) {
        val buffer = getRotationArray(rotation, flipHorizontal, flipVertical)
        val bBuffer = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder())
        val fBuffer = bBuffer.asFloatBuffer()
        fBuffer.put(buffer)
        fBuffer.flip()
        mTexture2CoordinatesBuffer = bBuffer
    }

    companion object {
        private const val VERTEX_SHADER = "" +
                "attribute vec4 position;\n" +
                "attribute vec4 inputTextureCoordinate;\n" +
                "attribute vec4 inputTextureCoordinate2;\n" +
                " \n" +
                "varying vec2 textureCoordinate;\n" +
                "varying vec2 textureCoordinate2;\n" +
                " \n" +
                "void main()\n" +
                "{\n" +
                "    gl_Position = position;\n" +
                "    textureCoordinate = inputTextureCoordinate.xy;\n" +
                "    textureCoordinate2 = inputTextureCoordinate2.xy;\n" +
                "}"
    }
}