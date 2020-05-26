package dev.shog.fe

import net.bramp.ffmpeg.FFmpeg
import net.bramp.ffmpeg.FFmpegExecutor
import net.bramp.ffmpeg.FFprobe
import net.bramp.ffmpeg.builder.FFmpegBuilder
import java.util.concurrent.LinkedBlockingQueue

/**
 * Houses FFMPEG and FFPROBE instances, and run s.
 */
object Executor {
    val ffprobe = FFprobe(probeFile.path)
    val ffmpeg = FFmpeg(ffmpegFile.path)

    fun runJob(builder: FFmpegBuilder) {
        Runnable {
            FFmpegExecutor(ffmpeg, ffprobe)
                    .createJob(builder)
                    .run()
        }.run()
    }
}