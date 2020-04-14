package com.tencent.lucky.transforms.util

import android.app.ActivityManager
import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES30
import android.opengl.GLUtils
import android.util.Log
import com.tencent.lucky.transforms.BuildConfig
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer

/**
 * OpenGl常用工具类
 * Some OpenGL utility functions.
 **/
object GLUtil {
    private const val TAG = "GlUtil"

    private const val NO_TEXTURE = -1
    private const val SIZEOF_FLOAT = 4

    private var DEBUG = BuildConfig.DEBUG

    /**
     * 通过传入的顶点着色器和片段着色器，创建渲染处理流
     * Creates a new program from the supplied vertex and fragment shaders.
     *
     * @return A handle to the program, or 0 on failure.
     */
    fun createProgram(vertexSource: String, fragmentSource: String): Int {
        val vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, vertexSource)
        if (vertexShader == 0) {
            return 0
        }
        val pixelShader = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentSource)
        if (pixelShader == 0) {
            return 0
        }
        var program = GLES30.glCreateProgram()
        checkGlError("glCreateProgram")
        if (program == 0) {
            Log.e(TAG, "Could not create program")
        } else {
            GLES30.glAttachShader(program, vertexShader)
            checkGlError("glAttachShader")
            GLES30.glAttachShader(program, pixelShader)
            checkGlError("glAttachShader")
            GLES30.glLinkProgram(program)
            val linkStatus = IntArray(1)
            GLES30.glGetProgramiv(program, GLES30.GL_LINK_STATUS, linkStatus, 0)
            if (linkStatus[0] != GLES30.GL_TRUE) {
                Log.e(TAG, "Could not link program: ")
                Log.e(TAG, GLES30.glGetProgramInfoLog(program))
                GLES30.glDeleteProgram(program)
                program = 0
            }
        }
        GLES30.glDeleteShader(vertexShader)
        GLES30.glDeleteShader(pixelShader)
        return program
    }

    /**
     * 预处理指定类型着色器
     * Compiles the provided shader source.
     *
     * @return A handle to the shader, or 0 on failure.
     */
    fun loadShader(shaderType: Int, source: String): Int {
        var shader = GLES30.glCreateShader(shaderType)
        checkGlError("glCreateShader type=$shaderType")
        if (shader == 0) {
            Log.e(TAG, "Create shader fail")
        } else {
            GLES30.glShaderSource(shader, source)
            GLES30.glCompileShader(shader)
            val compiled = IntArray(1)
            GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compiled, 0)
            if (compiled[0] == 0) {
                Log.e(TAG, "Could not compile shader $shaderType:")
                Log.e(TAG, " " + GLES30.glGetShaderInfoLog(shader))
                GLES30.glDeleteShader(shader)
                shader = 0
            }
        }
        return shader
    }

    /**
     * 纹理坐标、顶点坐标、纹理等对应id的合理性校验方法
     * Checks to see if the location we obtained is valid.  GLES returns -1 if a label
     * could not be found, but does not set the GL error.
     *
     * Throws a RuntimeException if the location is invalid.
     */
    fun checkLocation(location: Int, label: String) {
        if (location < 0) {
            throw RuntimeException("Unable to locate '$label' in program")
        }
    }

    /**
     * 根据raw资源文件创建渲染纹理
     * Creates a texture from raw data.
     *
     * @param data   Image data, in a "direct" ByteBuffer.
     * @param width  Texture width, in pixels (not bytes).
     * @param height Texture height, in pixels.
     * @param format Image data format (use constant appropriate for glTexImage2D(), e.g. GL_RGBA).
     * @return Handle to texture.
     */
    fun createTexture(data: ByteBuffer?, width: Int, height: Int, format: Int): Int {
        val textures = IntArray(1)
        GLES30.glGenTextures(1, textures, 0)
        checkGlError("glGenTextures")
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textures[0])
        checkGlError("glBindTexture")
        GLES30.glTexParameteri( /*texture-type*/
            GLES30.GL_TEXTURE_2D,  /*filter-type*/
            GLES30.GL_TEXTURE_MIN_FILTER,  /*algorithm*/
            GLES30.GL_LINEAR
        )
        GLES30.glTexParameteri( /*texture-type*/
            GLES30.GL_TEXTURE_2D,  /*filter-type*/
            GLES30.GL_TEXTURE_MAG_FILTER,  /*algorithm*/
            GLES30.GL_LINEAR
        )
        checkGlError("glTexParameteri")
        GLES30.glTexImage2D( /*aim*/
            GLES30.GL_TEXTURE_2D,  /*level*/
            0,  /*detail*/
            format, width, height,  /*border*/
            0,  /*pixel-format*/
            format,  /*pixel-type*/
            GLES30.GL_UNSIGNED_BYTE,  /*pixel-data*/
            data
        )
        checkGlError("glTexImage2D")
        return textures[0]
    }

    /**
     * 根据Bitmap创建渲染纹理
     * Creates a texture from bitmap data.
     *
     * @param img       Image data, in Bitmap format.
     * @param usedTexId an exist texture id ,if u want to use in this place.
     * @param recycle   should method recycle the bitmap when done with it.
     * @return Handle to texture.
     */
    fun createTexture(img: Bitmap, usedTexId: Int, recycle: Boolean): Int {
        val textures = IntArray(1)
        if (usedTexId == NO_TEXTURE) {
            GLES30.glGenTextures(1, textures, 0)
            checkGlError("glGenTextures")
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textures[0])
            checkGlError("glBindTexture")
            GLES30.glTexParameterf(
                GLES30.GL_TEXTURE_2D,
                GLES30.GL_TEXTURE_MIN_FILTER,
                GLES30.GL_LINEAR.toFloat()
            )
            GLES30.glTexParameterf(
                GLES30.GL_TEXTURE_2D,
                GLES30.GL_TEXTURE_MAG_FILTER,
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
            checkGlError("glTexParameteri")
            GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, img, 0)
            checkGlError("glTexImage2D")
        } else {
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, usedTexId)
            checkGlError("glBindTexture")
            GLUtils.texSubImage2D(GLES30.GL_TEXTURE_2D, 0, 0, 0, img)
            checkGlError("glTexImage2D")
            textures[0] = usedTexId
        }
        if (recycle) {
            img.recycle()
        }
        return textures[0]
    }

    /**
     * 根据配置好的数据生成渲染纹理
     * Creates a texture from buffer data.
     *
     * @param data      Image data, in IntBuffer.
     * @param width     Texture width, in pixels (not bytes).
     * @param height    Texture height, in pixels.
     * @param usedTexId an exist texture id ,if u want to use in this place.
     * @return Handle to texture.
     */
    fun createTexture(data: IntBuffer, width: Int, height: Int, usedTexId: Int): Int {
        val textures = IntArray(1)
        if (usedTexId == NO_TEXTURE) {
            GLES30.glGenTextures(1, textures, 0)
            checkGlError("glGenTextures")
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textures[0])
            checkGlError("glBindTexture")
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
            checkGlError("glTexParameterf")
            GLES30.glTexImage2D(
                GLES30.GL_TEXTURE_2D,
                0,
                GLES30.GL_RGBA, width, height,
                0,
                GLES30.GL_RGBA,
                GLES30.GL_UNSIGNED_BYTE,
                data
            )
            checkGlError("glTexImage2D")
        } else {
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, usedTexId)
            checkGlError("glBindTexture")
            GLES30.glTexSubImage2D(
                GLES30.GL_TEXTURE_2D,
                0,
                0, 0, width, height,
                GLES30.GL_RGBA,
                GLES30.GL_UNSIGNED_BYTE,
                data
            )
            checkGlError("glTexSubImage2D")
            textures[0] = usedTexId
        }
        return textures[0]
    }

    /**
     * 申请浮点缓存，并向其中压入数据
     * Allocates a direct float buffer, and populates it with the float array data.
     */
    fun createFloatBuffer(coords: FloatArray): FloatBuffer {
        // Allocate a direct ByteBuffer, using 4 bytes per float, and copy coords into it.
        val bb = ByteBuffer.allocateDirect(coords.size * SIZEOF_FLOAT)
        bb.order(ByteOrder.nativeOrder())
        val fb = bb.asFloatBuffer()
        fb.put(coords)
        fb.position(0)
        return fb
    }

    /**
     * convert texture to bitmap
     */
    fun textureToBitmap(texture: Int, width: Int, height: Int, flipY: Boolean = false): Bitmap {
        val buffer = ByteBuffer.allocate(width * height * 4)
        val frameBuffers = IntArray(1)
        GLES30.glGenFramebuffers(frameBuffers.size, frameBuffers, 0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture)
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, frameBuffers[0])
        GLES30.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, GLES30.GL_COLOR_ATTACHMENT0, GLES30.GL_TEXTURE_2D, texture, 0)
        GLES30.glReadPixels(0, 0, width, height, GLES30.GL_RGBA, GLES30.GL_UNSIGNED_BYTE, buffer)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        buffer.position(0)
        bitmap.copyPixelsFromBuffer(buffer)
        GLES30.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, GLES30.GL_COLOR_ATTACHMENT0, GLES30.GL_TEXTURE_2D, 0, 0)
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0)
        GLES30.glDeleteFramebuffers(frameBuffers.size, frameBuffers, 0)
        return if (flipY) {
            bitmap.flipVertical()
        } else {
            bitmap
        }
    }

    /**
     * save current framebuffer to bitmap
     */
    fun frameToBitmap(width: Int, height: Int, flipY: Boolean = false): Bitmap {
        val buffer = ByteBuffer.allocate(width * height * 4)
        GLES30.glReadPixels(0, 0, width, height, GLES30.GL_RGBA, GLES30.GL_UNSIGNED_BYTE, buffer)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        buffer.position(0)
        bitmap.copyPixelsFromBuffer(buffer)
        return if (flipY) {
            bitmap.flipVertical()
        } else {
            bitmap
        }
    }

    /**
     * OpenGL错误检测方法
     * Checks to see if a GLES error has been raised.
     */
    fun checkGlError(op: String) {
        val error = GLES30.glGetError()
        if (error != GLES30.GL_NO_ERROR) {
            val msg = op + ": glError 0x" + Integer.toHexString(error)
            Log.e(TAG, msg)
            if (DEBUG) {
                throw RuntimeException(msg)
            }
        }
    }

    /**
     * 打印当前OpenGl版本信息到日志
     * Writes GL version info to the log.
     */
    fun logVersionInfo() {
        Log.i(TAG, "vendor  : " + GLES30.glGetString(GLES30.GL_VENDOR))
        Log.i(TAG, "renderer: " + GLES30.glGetString(GLES30.GL_RENDERER))
        Log.i(TAG, "version : " + GLES30.glGetString(GLES30.GL_VERSION))
        if (DEBUG) {
            val values = IntArray(1)
            GLES30.glGetIntegerv(GLES30.GL_MAJOR_VERSION, values, 0)
            val majorVersion = values[0]
            GLES30.glGetIntegerv(GLES30.GL_MINOR_VERSION, values, 0)
            val minorVersion = values[0]
            if (GLES30.glGetError() == GLES30.GL_NO_ERROR) {
                Log.i(TAG, "iversion: $majorVersion.$minorVersion")
            }
        }
    }

    /**
     * 是否支持OpenGL ES3
     */
    fun supportsOpenGLES3(context: Context) {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val configurationInfo = activityManager.deviceConfigurationInfo
        check(configurationInfo.reqGlEsVersion >= 0x30000) { "OpenGL ES 3.0 is not supported on this phone." }
    }
}