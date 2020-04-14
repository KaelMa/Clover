package com.tencent.lucky.transforms.base

import android.graphics.Bitmap
import android.opengl.EGL14
import android.util.Log
import com.tencent.lucky.transforms.util.GLUtil
import com.tencent.lucky.transforms.util.NativeUtils.saveToBitmap
import javax.microedition.khronos.egl.*
import javax.microedition.khronos.opengles.GL10

/**
 * PixelBuffer surface
 */
class PixelBuffer(private val width: Int, private val height: Int) {

    private var renderer: Renderer? = null
    private val mThreadOwner: String

    private val egl10: EGL10
    private val eglDisplay: EGLDisplay
    private lateinit var eglConfigs: Array<EGLConfig?>
    private val eglConfig: EGLConfig?
    private val eglContext: EGLContext
    private val eglSurface: EGLSurface
    private val gl10: GL10

    companion object {
        private const val TAG = "PixelBuffer"
        private const val LIST_CONFIGS = false
    }

    init {
        val version = IntArray(2)

        val contextAttrs = intArrayOf(
            EGL14.EGL_CONTEXT_CLIENT_VERSION, 3,
            EGL10.EGL_NONE
        )
        val surfaceAttrs = intArrayOf(
            EGL10.EGL_WIDTH, width,
            EGL10.EGL_HEIGHT, height,
            EGL10.EGL_NONE
        )
        egl10 = EGLContext.getEGL() as EGL10
        eglDisplay = egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY)
        egl10.eglInitialize(eglDisplay, version)
        eglConfig = chooseConfig()
        eglContext = egl10.eglCreateContext(eglDisplay, eglConfig, EGL10.EGL_NO_CONTEXT, contextAttrs)
        eglSurface = egl10.eglCreatePbufferSurface(eglDisplay, eglConfig, surfaceAttrs)
        egl10.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)
        gl10 = eglContext.gl as GL10
        // Record thread owner of OpenGL context
        mThreadOwner = Thread.currentThread().name
    }

    /**
     * set renderer
     */
    fun setRenderer(renderer: Renderer?) {
        // thread check
        assertThread()

        this.renderer = renderer
        this.renderer?.onInit()
        this.renderer?.onSurfaceChanged(width, height)
    }

    /**
     * get bitmap from pixel buffer
     */
    fun getBitmap(): Bitmap? {
        if (renderer == null) {
            Log.e(TAG, "getBitmap: Renderer was not set.")
            return null
        }
        assertThread()

        val needFlip = renderer?.onDrawFrame()?: true

        return GLUtil.frameToBitmap(width, height, needFlip)
    }

    /**
     * destroy egl context
     */
    fun destroy() {
        renderer?.onRelease()

        egl10.eglMakeCurrent(eglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT)
        egl10.eglDestroySurface(eglDisplay, eglSurface)
        egl10.eglDestroyContext(eglDisplay, eglContext)
        egl10.eglTerminate(eglDisplay)
    }

    private fun convertToBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        saveToBitmap(bitmap)
        return bitmap
    }

    private fun chooseConfig(): EGLConfig? {
        val attribList = intArrayOf(
            EGL10.EGL_DEPTH_SIZE, 0,
            EGL10.EGL_STENCIL_SIZE, 0,
            EGL10.EGL_RED_SIZE, 8,
            EGL10.EGL_GREEN_SIZE, 8,
            EGL10.EGL_BLUE_SIZE, 8,
            EGL10.EGL_ALPHA_SIZE, 8,
            EGL10.EGL_RENDERABLE_TYPE, 4,
            EGL10.EGL_NONE
        )

        val numConfig = IntArray(1)
        egl10.eglChooseConfig(eglDisplay, attribList, null, 0, numConfig)
        val configSize = numConfig[0]
        eglConfigs = arrayOfNulls(configSize)
        egl10.eglChooseConfig(eglDisplay, attribList, eglConfigs, configSize, numConfig)
        if (LIST_CONFIGS) {
            listConfig()
        }
        return eglConfigs[0]
    }

    private fun listConfig() {
        Log.i(TAG, "Config List {")
        for (config in eglConfigs) {
            // Expand on this logic to dump other attributes
            val d: Int = getConfigAttrib(config, EGL10.EGL_DEPTH_SIZE)
            val s: Int = getConfigAttrib(config, EGL10.EGL_STENCIL_SIZE)
            val r: Int = getConfigAttrib(config, EGL10.EGL_RED_SIZE)
            val g: Int = getConfigAttrib(config, EGL10.EGL_GREEN_SIZE)
            val b: Int = getConfigAttrib(config, EGL10.EGL_BLUE_SIZE)
            val a: Int = getConfigAttrib(config, EGL10.EGL_ALPHA_SIZE)
            Log.i(TAG, "    <d,s,r,g,b,a> = <$d,$s,$r,$g,$b,$a>")
        }
        Log.i(TAG, "}")
    }

    private fun getConfigAttrib(config: EGLConfig?, attribute: Int): Int {
        val value = IntArray(1)
        return if (egl10.eglGetConfigAttrib(eglDisplay, config, attribute, value)) value[0] else 0
    }

    private fun assertThread() {
        check(Thread.currentThread().name == mThreadOwner) { "must call setRenderer on gl thread" }
    }
}