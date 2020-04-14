package com.tencent.lucky.tool.extract

import android.graphics.Bitmap
import java.util.concurrent.ExecutionException

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: CloverUtil
 * Author: Kaelma
 * Date: 2020/10/1
 * Description: Clover Util Facade类
 */
object CloverUtil {
    /**
     * 使用mediaCodec + OpenGL取帧
     * @param videoPath 视频路径
     * @param timeUs 时间戳
     * @return bitmap 视频抽帧
     */
    @Throws(ExecutionException::class, InterruptedException::class)
    fun getFrameAtTime(videoPath: String, timeUs: Long): Bitmap? {
        return ExtractUtil.getFrameAtTime(videoPath, timeUs)
    }
}