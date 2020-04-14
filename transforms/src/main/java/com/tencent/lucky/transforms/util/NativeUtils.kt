package com.tencent.lucky.transforms.util

import android.graphics.Bitmap

object NativeUtils {

    init {
        System.loadLibrary("transform-utils")
    }

    /**
     * YUV To RBGA
     */
    external fun yuvToRBGA(yuv: ByteArray?, width: Int, height: Int, out: IntArray?)

    /**
     * YUV To ARBG
     */
    external fun yuvToARBG(yuv: ByteArray?, width: Int, height: Int, out: IntArray?)

    /**
     * save current texture to bitmap,
     * must be call on gl thread
     */
    external fun saveToBitmap(srcBitmap: Bitmap?)
}