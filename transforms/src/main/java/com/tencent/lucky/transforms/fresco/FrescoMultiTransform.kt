package com.tencent.lucky.transforms.fresco

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.facebook.imagepipeline.request.BasePostprocessor

class FrescoMultiTransform(private val mProcessors: List<BasePostprocessor>)
    : BasePostprocessor() {

    override fun process(dest: Bitmap, src: Bitmap) {
        val canvas = Canvas(dest)
        val paint = Paint()
        canvas.drawBitmap(src, 0f, 0f, paint)

        for (processor in mProcessors) {
            processor.process(dest, dest)
        }
    }
}