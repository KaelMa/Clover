package com.tencent.lucky.transforms.filter

import android.graphics.PointF
import android.opengl.GLES30
import android.util.Log
import com.tencent.lucky.transforms.base.BaseFilter
import com.tencent.lucky.transforms.util.GLUtil.createProgram
import com.tencent.lucky.transforms.util.TextureRotationUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.util.*

/**
 * 基于GPUImage filter
 */
open class GPUImageFilter(
    vertexShader: String = NO_FILTER_VERTEX_SHADER,
    fragmentShader: String = NO_FILTER_FRAGMENT_SHADER
) : BaseFilter {

    companion object {

        /** 无渲染的顶点着色器 vertex shader without filter  */
        const val NO_FILTER_VERTEX_SHADER = "" +
                "attribute vec4 position;\n" +
                "attribute vec4 inputTextureCoordinate;\n" +
                "varying vec2 textureCoordinate;\n" +
                "void main()\n" +
                "{\n" +
                "    gl_Position = position;\n" +
                "    textureCoordinate = inputTextureCoordinate.xy;\n" +
                "}"
        /** 无渲染的片段着色器 fragment shader without filter  */
        const val NO_FILTER_FRAGMENT_SHADER = "" +
                "varying highp vec2 textureCoordinate;\n" +
                "uniform sampler2D inputImageTexture;\n" +
                "void main()\n" +
                "{\n" +
                "     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n" +
                "}"

        const val DEFAULT_NO_IMAGE = -1

        val CUBE = floatArrayOf(
            -1.0f, -1.0f,
            1.0f, -1.0f,
            -1.0f, 1.0f,
            1.0f, 1.0f
        )
    }

    private val mRunOnDraw: LinkedList<Runnable> = LinkedList()
    private val mVertexShader: String = vertexShader
    private val mFragmentShader: String = fragmentShader

    var program = 0
    var attribPosition = 0
    var attribTextureCoordinate = 0
    var uniformTexture = 0
    var outputWidth = 0
    var outputHeight = 0
    var isInitialized = false

    // 顶点与纹理坐标
    private var mGLCubeBuffer: FloatBuffer = ByteBuffer.allocateDirect(CUBE.size * 4)
        .order(ByteOrder.nativeOrder()).asFloatBuffer()
    private var mGLTextureBuffer: FloatBuffer = ByteBuffer.allocateDirect(TextureRotationUtil.TEXTURE_NO_ROTATION.size * 4)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer()

    init {
        mGLCubeBuffer.put(CUBE).position(0)
        mGLTextureBuffer.put(TextureRotationUtil.TEXTURE_NO_ROTATION).position(0)
    }

    /*生命周期流程==================================================================================*/
    override fun init() {
        onInit()
        this.isInitialized = true
        onInitialized()
    }

    override fun destroy() {
        this.isInitialized = false
        GLES30.glDeleteProgram(program)
        onDestroy()
    }

    /*生命周期======================================================================================*/
    open fun onInit() {
        program = createProgram(mVertexShader, mFragmentShader)
        attribPosition = GLES30.glGetAttribLocation(program, "position")
        attribTextureCoordinate = GLES30.glGetAttribLocation(program, "inputTextureCoordinate")
        uniformTexture = GLES30.glGetUniformLocation(program, "inputImageTexture")
        this.isInitialized = true
    }

    open fun onInitialized() {

    }

    open fun onDestroy() {

    }

    override fun onOutputSizeChanged(width: Int, height: Int) {
        outputWidth = width
        outputHeight = height
    }

    override fun draw(inputTexName: Int): Boolean {
        onDraw(inputTexName, mGLCubeBuffer, mGLTextureBuffer)
        return true
    }

    open fun onDraw(textureId: Int, cubeBuffer: FloatBuffer, textureBuffer: FloatBuffer) {
        GLES30.glUseProgram(program)
        runPendingOnDrawTasks()
        if (!this.isInitialized) {
            return
        }
        cubeBuffer.position(0)
        textureBuffer.position(0)
        GLES30.glVertexAttribPointer(attribPosition, 2, GLES30.GL_FLOAT, false, 0, cubeBuffer)
        GLES30.glVertexAttribPointer(
            attribTextureCoordinate,
            2,
            GLES30.GL_FLOAT,
            false,
            0,
            textureBuffer
        )
        GLES30.glEnableVertexAttribArray(attribPosition)
        GLES30.glEnableVertexAttribArray(attribTextureCoordinate)
        if (textureId != DEFAULT_NO_IMAGE) {
            GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId)
            GLES30.glUniform1i(uniformTexture, 0)
        }
        onDrawArraysPre()
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, 4)
        GLES30.glDisableVertexAttribArray(attribPosition)
        GLES30.glDisableVertexAttribArray(attribTextureCoordinate)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0)
    }

    open fun runPendingOnDrawTasks() {
        while (!mRunOnDraw.isEmpty()) {
            try {
                val first = mRunOnDraw.removeFirst()
                first?.run()
            } catch (e: NoSuchElementException) {
                Log.e("GPUImageFilter", e.toString())
                return
            }
        }
    }

    open fun onDrawArraysPre() {}

    open fun runOnDraw(runnable: Runnable) {
        synchronized(mRunOnDraw) {
            mRunOnDraw.addLast(runnable)
        }
    }

    /*参数配置方法==================================================================================*/
    fun setInteger(location: Int, intValue: Int) {
        runOnDraw(Runnable {
            GLES30.glUniform1i(location, intValue)
        })
    }

    fun setFloat(location: Int, floatValue: Float) {
        runOnDraw(Runnable {
            GLES30.glUniform1f(location, floatValue)
        })
    }

    fun setFloatVec2(location: Int, arrayValue: FloatArray?) {
        runOnDraw(Runnable {
            GLES30.glUniform2fv(location, 1, FloatBuffer.wrap(arrayValue))
        })
    }

    fun setFloatVec3(location: Int, arrayValue: FloatArray?) {
        runOnDraw(Runnable {
            GLES30.glUniform3fv(location, 1, FloatBuffer.wrap(arrayValue))
        })
    }

    fun setFloatVec4(location: Int, arrayValue: FloatArray?) {
        runOnDraw(Runnable {
            GLES30.glUniform4fv(location, 1, FloatBuffer.wrap(arrayValue))
        })
    }

    fun setFloatArray(location: Int, arrayValue: FloatArray) {
        runOnDraw(Runnable {
            GLES30.glUniform1fv(
                location,
                arrayValue.size,
                FloatBuffer.wrap(arrayValue)
            )
        })
    }

    fun setPoint(location: Int, point: PointF) {
        runOnDraw(Runnable {
            val vec2 = FloatArray(2)
            vec2[0] = point.x
            vec2[1] = point.y
            GLES30.glUniform2fv(location, 1, vec2, 0)
        })
    }

    fun setUniformMatrix3f(location: Int, matrix: FloatArray?) {
        runOnDraw(Runnable {
            GLES30.glUniformMatrix3fv(location, 1, false, matrix, 0)
        })
    }

    fun setUniformMatrix4f(location: Int, matrix: FloatArray?) {
        runOnDraw(Runnable {
            GLES30.glUniformMatrix4fv(location, 1, false, matrix, 0)
        })
    }
}