package com.tencent.lucky.tool.extract

import android.graphics.Bitmap
import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.util.Log
import com.tencent.lucky.transforms.BuildConfig
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: ExtractUtil
 * Author: kaelma
 * Date: 2020/3/5 6:42 PM
 * Description: 使用MediaCodec + OpenGL抽帧功能类
 */
internal object ExtractUtil {
    private const val TAG = "ExtractUtil"
    private val VERBOSE = BuildConfig.DEBUG

    /**
     * 使用mediaCodec + OpenGL取帧
     * @param videoPath 视频路径
     * @param timeUs 时间戳
     * @return bitmap 视频抽帧
     */
    @Throws(ExecutionException::class, InterruptedException::class)
    fun getFrameAtTime(videoPath: String, timeUs: Long): Bitmap? {
        val executor = Executors.newSingleThreadExecutor()
        val result = executor.submit<Bitmap?> {
            getFrameAtTimeInner(videoPath, timeUs)
        }
        return result.get()
    }

    /**
     * 使用mediaCodec取帧，注意调用线程需要没有Looper
     */
    @Throws(IOException::class)
    private fun getFrameAtTimeInner(videoPath: String, timeUs: Long): Bitmap? {
        var decoder: MediaCodec? = null
        var outputSurface: CodecOutputSurface? = null
        var extractor: MediaExtractor? = null
        return try {
            val inputFile = File(videoPath)
            if (!inputFile.canRead()) {
                throw FileNotFoundException("Unable to read $inputFile")
            }
            extractor = MediaExtractor()
            extractor.setDataSource(inputFile.toString())
            val trackIndex = selectTrack(extractor)
            if (trackIndex < 0) {
                throw RuntimeException("No video track found in $inputFile")
            }
            extractor.selectTrack(trackIndex)
            val format = extractor.getTrackFormat(trackIndex)
            var rotation = 0
            if (format.containsKey(MediaFormat.KEY_ROTATION)) {
                rotation = format.getInteger(MediaFormat.KEY_ROTATION)
            }
            var videoWidth = format.getInteger(MediaFormat.KEY_WIDTH)
            var videoHeight = format.getInteger(MediaFormat.KEY_HEIGHT)
            if (rotation == 90 || rotation == 270) {
                val temp = videoWidth
                videoWidth = videoHeight
                videoHeight = temp
            }

            // use width/height from the MediaFormat to get full-size frames.
            outputSurface = CodecOutputSurface(videoWidth, videoHeight)

            // Create a MediaCodec decoder, and configure it with the MediaFormat from the
            // extractor.  It's very important to use the format from the extractor because
            // it contains a copy of the CSD-0/CSD-1 codec-specific data chunks.
            val mime = format.getString(MediaFormat.KEY_MIME)
            decoder = MediaCodec.createDecoderByType(mime)
            decoder.configure(format, outputSurface.surface, null, 0)
            decoder.start()
            doExtract(extractor, trackIndex, decoder, outputSurface, timeUs)
        } finally {
            // release everything we grabbed
            outputSurface?.release()
            decoder?.stop()
            decoder?.release()
            extractor?.release()
        }
    }

    /**
     * Selects the video track, if any.
     *
     * @return the track index, or -1 if no video track is found.
     */
    private fun selectTrack(extractor: MediaExtractor): Int {
        // Select the first video track we find, ignore the rest.
        val numTracks = extractor.trackCount
        for (i in 0 until numTracks) {
            val format = extractor.getTrackFormat(i)
            val mime = format.getString(MediaFormat.KEY_MIME)
            if (mime != null && mime.startsWith("video/")) {
                if (VERBOSE) {
                    Log.d(TAG, "Extractor selected track $i ($mime): $format")
                }
                return i
            }
        }
        return -1
    }

    /**
     * Work loop.
     */
    @Throws(IOException::class)
    private fun doExtract(
        extractor: MediaExtractor,
        trackIndex: Int,
        decoder: MediaCodec,
        outputSurface: CodecOutputSurface,
        seekTimeUs: Long
    ): Bitmap? {
        var result: Bitmap? = null
        val TIMEOUT_USEC = 10000
        val decoderInputBuffers = decoder.inputBuffers
        val info = MediaCodec.BufferInfo()
        var inputChunk = 0
        val decodeCount = 0
        var outputDone = false
        var inputDone = false

        while (!outputDone) {
            if (VERBOSE) {
                Log.d(TAG, "loop")
            }

            // Feed more data to the decoder.
            if (!inputDone) {
                val inputBufIndex = decoder.dequeueInputBuffer(TIMEOUT_USEC.toLong())
                if (inputBufIndex >= 0) {
                    val inputBuf = decoderInputBuffers[inputBufIndex]
                    // Read the sample data into the ByteBuffer.  This neither respects nor
                    // updates inputBuf's position, limit, etc.
                    extractor.seekTo(seekTimeUs, MediaExtractor.SEEK_TO_CLOSEST_SYNC)
                    val chunkSize = extractor.readSampleData(inputBuf, 0)
                    if (chunkSize < 0) {
                        // End of stream -- send empty frame with EOS flag set.
                        decoder.queueInputBuffer(inputBufIndex, 0, 0, 0L, MediaCodec.BUFFER_FLAG_END_OF_STREAM)
                        inputDone = true
                        if (VERBOSE){
                            Log.d(TAG, "sent input EOS")
                        }
                    } else {
                        if (extractor.sampleTrackIndex != trackIndex) {
                            Log.w(TAG, "WEIRD: got sample from track " + extractor.sampleTrackIndex + ", expected " + trackIndex)
                        }
                        val presentationTimeUs = extractor.sampleTime
                        decoder.queueInputBuffer(inputBufIndex, 0, chunkSize, presentationTimeUs, 0 /*flags*/)
                        if (VERBOSE) {
                            Log.d(TAG, "submitted frame $inputChunk to dec, size=$chunkSize")
                        }
                        inputChunk++
                        extractor.advance()
                    }
                } else {
                    if (VERBOSE) {
                        Log.d(TAG, "input buffer not available")
                    }
                }
            }
            if (!outputDone) {
                val decoderStatus = decoder.dequeueOutputBuffer(info, TIMEOUT_USEC.toLong())
                if (decoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER) {
                    // no output available yet
                    if (VERBOSE) {
                        Log.d(TAG, "no output from decoder available")
                    }
                } else if (decoderStatus == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                    // not important for us, since we're using Surface
                    if (VERBOSE) {
                        Log.d(TAG, "decoder output buffers changed")
                    }
                } else if (decoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                    val newFormat = decoder.outputFormat
                    if (VERBOSE) {
                        Log.d(TAG, "decoder output format changed: $newFormat")
                    }
                } else if (decoderStatus < 0) {
                    Log.e(TAG, "unexpected result from decoder.dequeueOutputBuffer: $decoderStatus")
                } else {
                    // decoderStatus >= 0
                    if (VERBOSE){
                        Log.d(TAG, "surface decoder given buffer " + decoderStatus + " (size=" + info.size + ")")
                    }

                    if (info.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0) {
                        if (VERBOSE) {
                            Log.d(TAG, "output EOS")
                        }
                        outputDone = true
                    }
                    val doRender = info.size != 0

                    // As soon as we call releaseOutputBuffer, the buffer will be forwarded
                    // to SurfaceTexture to convert to a texture.  The API doesn't guarantee
                    // that the texture will be available before the call returns, so we
                    // need to wait for the onFrameAvailable callback to fire.
                    decoder.releaseOutputBuffer(decoderStatus, doRender)
                    if (doRender) {
                        if (VERBOSE) {
                            Log.d(TAG, "awaiting decode of frame $decodeCount")
                        }
                        outputSurface.awaitNewImage()
                        outputSurface.drawImage(false)
                        result = outputSurface.saveBitmap(true)
                        if (VERBOSE) {
                            Log.d(TAG, "result bitmap: $result")
                        }
                        outputDone = true
                    }
                }
            }
        }
        return result
    }
}