package com.tencent.lucky.transforms.base

import android.graphics.Bitmap
import android.graphics.Canvas
import android.opengl.GLES30
import com.tencent.lucky.transforms.util.GLUtil
import java.util.*

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: BaseRender
 * Author: kaelma
 * Date: 2020/4/13 7:12 PM
 * Description: 图片渲染类
 */
class ImageRenderer(filter: BaseFilter): Renderer {

    private var mFilter: BaseFilter = filter
    private var mGLTextureId = -1

    private val mRunOnDraw: Queue<Runnable> = LinkedList()
    private val mEndOnDraw: Queue<Runnable> = LinkedList()

    var outputWidth = 0
    private var outputHeight = 0
    private var mBGR = 0f
    private var mBGG = 0f
    private var mBGB = 0f

    override fun onInit() {
        GLES30.glClearColor(mBGR, mBGG, mBGB, 1f)
        GLES30.glDisable(GLES30.GL_DEPTH_TEST)
        mFilter.init()
    }

    override fun onSurfaceChanged(width: Int, height: Int) {
        outputWidth = width
        outputHeight = height
        GLES30.glViewport(0, 0, outputWidth, outputHeight)
        mFilter.onOutputSizeChanged(width, height)
    }

    override fun onDrawFrame(): Boolean {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)
        runAll(mRunOnDraw)
        val needFlip = mFilter.draw(mGLTextureId)
        runAll(mEndOnDraw)
        return needFlip
    }

    override fun onRelease() {
        destroyTexture()
        mFilter.destroy()
    }

    /**
     * add to RunOnDraw queue
     */
    fun runBeforeDraw(runnable: Runnable) {
        synchronized(mRunOnDraw) {
            mRunOnDraw.add(runnable)
        }
    }

    /**
     * add to EndOnDraw queue
     */
    fun runAfterDraw(runnable: Runnable) {
        synchronized(mEndOnDraw) {
            mEndOnDraw.add(runnable)
        }
    }

    /**
     * set source bitmap
     * @param bitmap
     */
    fun setImageBitmap(bitmap: Bitmap) {
        runBeforeDraw(Runnable {
            mGLTextureId = GLUtil.createTexture(bitmap, mGLTextureId, false)
        })
    }

    /**
     * set background color
     */
    fun setBackgroundColor(red: Float, green: Float, blue: Float) {
        mBGR = red
        mBGG = green
        mBGB = blue
    }

    /**
     * set filter
     * @param filter
     */
    fun setFilter(filter: BaseFilter) {
        runBeforeDraw(Runnable {
            mFilter.destroy()

            mFilter = filter
            mFilter.init()
        })
    }

    private fun runAll(queue: Queue<Runnable>) {
        synchronized(queue) {
            while (!queue.isEmpty()) {
                queue.poll().run()
            }
        }
    }

    private fun destroyTexture() {
        GLES30.glDeleteTextures(1, intArrayOf(mGLTextureId), 0)
        mGLTextureId = -1
    }
}