package com.tencent.lucky.tool.extract

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.SurfaceTexture
import android.opengl.EGL14
import android.opengl.EGLConfig
import android.opengl.GLES20
import android.util.Log
import android.view.Surface
import com.tencent.lucky.transforms.BuildConfig
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: CodecOutputSurface
 * Author: kaelma
 * Date: 2020/3/5 6:39 PM
 * Description: 管理SurfaceTexture与EglContext
 */
class CodecOutputSurface(width: Int, height: Int) : SurfaceTexture.OnFrameAvailableListener {

    companion object {
        private const val TAG = "STextureRender"
        private val VERBOSE = BuildConfig.DEBUG
        const val TIMEOUT_MS = 2500
    }

    private var mTextureRender: STextureRender? = null
    private var mSurfaceTexture: SurfaceTexture? = null

    var surface: Surface? = null
        private set

    private var mEGLDisplay = EGL14.EGL_NO_DISPLAY
    private var mEGLContext = EGL14.EGL_NO_CONTEXT
    private var mEGLSurface = EGL14.EGL_NO_SURFACE
    private var mWidth: Int
    private var mHeight: Int

    private val mFrameSyncObject = Object() // guards mFrameAvailable
    private var mFrameAvailable = false

    private var mPixelBuf: ByteBuffer? = null
    private val mFrameWrapper = FrameWrapper()

    /**
     * Creates a CodecOutputSurface backed by a pbuffer with the specified dimensions.  The
     * new EGL context and surface will be made current.  Creates a Surface that can be passed
     * to MediaCodec.configure().
     */
    init {
        require(!(width <= 0 || height <= 0))
        mWidth = width
        mHeight = height

        eglSetup()
        makeCurrent()
        setup()
    }

    /**
     * Creates interconnected instances of TextureRender, SurfaceTexture, and Surface.
     */
    private fun setup() {
        mTextureRender = STextureRender()
        mTextureRender!!.surfaceCreated()
        if (VERBOSE) {
            Log.d(TAG, "textureID=" + mTextureRender!!.getTextureId())
        }
        mSurfaceTexture = SurfaceTexture(mTextureRender!!.getTextureId())
        mSurfaceTexture!!.setOnFrameAvailableListener(this)
        surface = Surface(mSurfaceTexture)
        mPixelBuf = ByteBuffer.allocateDirect(mWidth * mHeight * 4)
        mPixelBuf?.order(ByteOrder.LITTLE_ENDIAN)
    }

    /**
     * Prepares EGL.  We want a GLES 2.0 context and a surface that supports pbuffer.
     */
    private fun eglSetup() {
        mEGLDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
        if (mEGLDisplay === EGL14.EGL_NO_DISPLAY) {
            throw RuntimeException("unable to get EGL14 display")
        }
        val version = IntArray(2)
        if (!EGL14.eglInitialize(mEGLDisplay, version, 0, version, 1)) {
            mEGLDisplay = null
            throw RuntimeException("unable to initialize EGL14")
        }

        // Configure EGL for pbuffer and OpenGL ES 3.0, 24-bit RGB.
        val attribList = intArrayOf(
            EGL14.EGL_RED_SIZE, 8,
            EGL14.EGL_GREEN_SIZE, 8,
            EGL14.EGL_BLUE_SIZE, 8,
            EGL14.EGL_ALPHA_SIZE, 8,
            EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
            EGL14.EGL_SURFACE_TYPE, EGL14.EGL_PBUFFER_BIT,
            EGL14.EGL_NONE
        )
        val configs =
            arrayOfNulls<EGLConfig>(1)
        val numConfigs = IntArray(1)
        if (!EGL14.eglChooseConfig(mEGLDisplay, attribList, 0, configs, 0, configs.size,
                numConfigs, 0)) {
            throw RuntimeException("unable to find RGB888+recordable ES2 EGL config")
        }

        // Configure context for OpenGL ES 3.0.
        val attrib_list = intArrayOf(
            EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
            EGL14.EGL_NONE
        )
        mEGLContext = EGL14.eglCreateContext(
            mEGLDisplay, configs[0], EGL14.EGL_NO_CONTEXT,
            attrib_list, 0
        )
        if (mEGLContext == null) {
            throw RuntimeException("null context")
        }

        // Create a pbuffer surface.
        val surfaceAttribs = intArrayOf(
            EGL14.EGL_WIDTH, mWidth,
            EGL14.EGL_HEIGHT, mHeight,
            EGL14.EGL_NONE
        )
        mEGLSurface = EGL14.eglCreatePbufferSurface(mEGLDisplay, configs[0], surfaceAttribs, 0)
        if (mEGLSurface == null) {
            throw RuntimeException("surface was null")
        }
    }

    /**
     * Discard all resources held by this class, notably the EGL context.
     */
    fun release() {
        if (mEGLDisplay !== EGL14.EGL_NO_DISPLAY) {
            EGL14.eglDestroySurface(mEGLDisplay, mEGLSurface)
            EGL14.eglDestroyContext(mEGLDisplay, mEGLContext)
            EGL14.eglReleaseThread()
            EGL14.eglTerminate(mEGLDisplay)
        }
        mEGLDisplay = EGL14.EGL_NO_DISPLAY
        mEGLContext = EGL14.EGL_NO_CONTEXT
        mEGLSurface = EGL14.EGL_NO_SURFACE
        surface!!.release()

        // this causes a bunch of warnings that appear harmless but might confuse someone:
        //  W BufferQueue: [unnamed-3997-2] cancelBuffer: BufferQueue has been abandoned!
        //mSurfaceTexture.release();
        mTextureRender = null
        surface = null
        mSurfaceTexture = null
    }

    /**
     * Makes our EGL context and surface current.
     */
    fun makeCurrent() {
        if (!EGL14.eglMakeCurrent(mEGLDisplay, mEGLSurface, mEGLSurface, mEGLContext)) {
            throw RuntimeException("eglMakeCurrent failed")
        }
    }

    /**
     * Latches the next buffer into the texture.  Must be called from the thread that created
     * the CodecOutputSurface object.  (More specifically, it must be called on the thread
     * with the EGLContext that contains the GL texture object used by SurfaceTexture.)
     */
    fun awaitNewImage() {
        synchronized(mFrameSyncObject) {
            while (!mFrameAvailable) {
                try {
                    // Wait for onFrameAvailable() to signal us.  Use a timeout to avoid
                    // stalling the test if it doesn't arrive.
                    mFrameSyncObject.wait(TIMEOUT_MS.toLong())
                    if (!mFrameAvailable) {
                        // if "spurious wakeup", continue while loop
                        throw RuntimeException("frame wait timed out")
                    }
                } catch (ie: InterruptedException) {
                    // shouldn't happen
                    throw RuntimeException(ie)
                }
            }
            mFrameAvailable = false
        }

        // Latch the data.
        mSurfaceTexture!!.updateTexImage()
    }

    /**
     * Draws the data from SurfaceTexture onto the current EGL surface.
     *
     * @param invert if set, render the image with Y inverted (0,0 in top left)
     */
    fun drawImage(invert: Boolean) {
        mFrameWrapper.initAndBindFBO(mWidth, mHeight)
        mTextureRender!!.drawFrame(mSurfaceTexture!!, invert)
    }

    // SurfaceTexture callback
    override fun onFrameAvailable(st: SurfaceTexture) {
        if (VERBOSE) {
            Log.d(TAG, "new frame available")
        }

        synchronized(mFrameSyncObject) {
            if (mFrameAvailable) {
                throw RuntimeException("mFrameAvailable already set, frame could be dropped")
            }
            mFrameAvailable = true
            mFrameSyncObject.notifyAll()
        }
    }

    fun saveBitmap(flipY: Boolean): Bitmap {
        mPixelBuf!!.rewind()
        GLES20.glReadPixels(
            0, 0, mWidth, mHeight, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE,
            mPixelBuf
        )
        var bmp = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888)
        mPixelBuf!!.rewind()
        bmp.copyPixelsFromBuffer(mPixelBuf)
        if (flipY) {
            bmp = flipBitmapY(bmp)
        }
        return bmp
    }

    private fun flipBitmapY(bitmap: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.setScale(1f, -1f)
        if (bitmap.isRecycled) {
            return bitmap
        }
        // createBitmap()里如果传入的bitmap被recycle了，会造成native crash，而不是抛Exception
        val ret =
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        bitmap.recycle()
        return ret
    }
}