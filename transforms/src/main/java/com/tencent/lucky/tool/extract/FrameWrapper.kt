package com.tencent.lucky.tool.extract

import android.opengl.GLES30
import android.opengl.GLES30.*

/**
 * Wrapper GL framebuffer with texture as color attachment,
 * with renderbuffer as depth attachment.
 * Created by kaelma on 2019/5/21.
 */
class FrameWrapper {

    var width: Int = 0
    var height: Int = 0

    private var framebufferName: Int = 0
    private var renderbufferName: Int = 0
    private var texName: Int = 0

    /**
     * init opengl resource
     */
    private fun setup(width: Int, height: Int) {
        val args = IntArray(1)

        glGetIntegerv(GL_MAX_TEXTURE_SIZE, args, 0)
        if (width > args[0] || height > args[0]) {
            throw IllegalArgumentException("GL_MAX_TEXTURE_SIZE " + args[0])
        }

        glGetIntegerv(GL_MAX_RENDERBUFFER_SIZE, args, 0)
        if (width > args[0] || height > args[0]) {
            throw IllegalArgumentException("GL_MAX_RENDERBUFFER_SIZE " + args[0])
        }

        glGetIntegerv(GL_FRAMEBUFFER_BINDING, args, 0)
        val saveFramebuffer = args[0]
        glGetIntegerv(GL_RENDERBUFFER_BINDING, args, 0)
        val saveRenderbuffer = args[0]
        glGetIntegerv(GL_TEXTURE_BINDING_2D, args, 0)
        val saveTexName = args[0]

        release()

        try {
            this.width = width
            this.height = height

            glGenFramebuffers(args.size, args, 0)
            framebufferName = args[0]
            glBindFramebuffer(GL_FRAMEBUFFER, framebufferName)

            glGenRenderbuffers(args.size, args, 0)
            renderbufferName = args[0]
            glBindRenderbuffer(GL_RENDERBUFFER, renderbufferName)
            glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT16, width, height)
            glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, renderbufferName)

            glGenTextures(args.size, args, 0)
            texName = args[0]
            glBindTexture(GL_TEXTURE_2D, texName)
            setupSampler(GL_TEXTURE_2D, GL_LINEAR, GL_LINEAR)
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, null)
            glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texName, 0)

            val status = glCheckFramebufferStatus(GL_FRAMEBUFFER)
            if (status != GL_FRAMEBUFFER_COMPLETE) {
                throw RuntimeException("Failed to initialize framebuffer object $status width:$width height:$height")
            }
        } catch (e: RuntimeException) {
            release()
            throw e
        }

        glBindFramebuffer(GL_FRAMEBUFFER, saveFramebuffer)
        glBindRenderbuffer(GL_RENDERBUFFER, saveRenderbuffer)
        glBindTexture(GL_TEXTURE_2D, saveTexName)
    }

    /**
     * clear opengl resource
     */
    private fun release() {
        val args = IntArray(1)
        args[0] = texName
        glDeleteTextures(args.size, args, 0)
        texName = 0
        args[0] = renderbufferName
        glDeleteRenderbuffers(args.size, args, 0)
        renderbufferName = 0
        args[0] = framebufferName
        glDeleteFramebuffers(args.size, args, 0)
        framebufferName = 0
    }

    /**
     * bind current framebuffer
     */
    private fun bindFBO() {
        glBindFramebuffer(GL_FRAMEBUFFER, framebufferName)
        glViewport(0, 0, width, height)
    }

    /**
     * init and bind current framebuffer
     */
    fun initAndBindFBO(width: Int, height: Int) {
        if (!glIsTexture(texName) || !glIsFramebuffer(framebufferName)) {
            setup(width, height)
        }
        bindFBO()
    }

    /**
     * set sampler for texture
     */
    private fun setupSampler(target: Int, mag: Int, min: Int) {
        GLES30.glTexParameterf(target, GLES30.GL_TEXTURE_MAG_FILTER, mag.toFloat())
        GLES30.glTexParameterf(target, GLES30.GL_TEXTURE_MIN_FILTER, min.toFloat())
        GLES30.glTexParameteri(target, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE)
        GLES30.glTexParameteri(target, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE)
    }

}
