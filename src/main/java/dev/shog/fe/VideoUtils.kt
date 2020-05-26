package dev.shog.fe

import net.bramp.ffmpeg.builder.FFmpegBuilder
import net.lingala.zip4j.core.ZipFile
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels
import kotlin.system.exitProcess


/**
 * FilmEditor temp directory.
 */
private val location = File("${System.getProperty("java.io.tmpdir")}${File.separator}filmeditor")

val probeFile = File("${location.path}${File.separator}ffprobe")
val playFile = File("${location.path}${File.separator}ffplay")
val ffmpegFile = File("${location.path}${File.separator}ffmpeg")

const val macOs = "ffmpeg-20200525-6268034-macos64-static"
const val windows = "ffmpeg-20200525-6268034-win64-static"

/**
 * Get either [macOs] or [windows] depending on OS.
 */
fun getFileName(): String =
        when {
            System.getProperty("os.name").toLowerCase().contains("win") -> windows

            System.getProperty("os.name").toLowerCase().contains("mac") -> macOs

            else -> {
                println("Invalid operating system.")
                exitProcess(-1)
            }
        }

/**
 * Get the URL where FFMPEG should be downloaded from.
 */
fun getUrl(): String {
    return when {
        System.getProperty("os.name").toLowerCase().contains("win") -> "https://ffmpeg.zeranoe.com/builds/win64/static/${getFileName()}.zip"

        System.getProperty("os.name").toLowerCase().contains("mac") -> "https://ffmpeg.zeranoe.com/builds/macos64/static/${getFileName()}.zip"

        else -> {
            println("Invalid operating system.")
            exitProcess(-1)
        }
    }
}

/**
 * Download FFMPEG
 */
fun downloadDependencies() {
    println("Checking if FFMPEG needs to be downloaded...")

    if (probeFile.exists() && ffmpegFile.exists() && playFile.exists()) {
        println("FFMPEG found!")
        hasFfmpeg = true
        return
    }

    downloadPanel.isVisible = true

    val urlStr = getUrl()

    println("FFMPEG not found, downloading from $urlStr")

    if (location.exists()) {
        location.deleteRecursively()
    }

    location.mkdirs()

    System.setProperty("http.agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:77.0) Gecko/20100101 Firefox/77.0")

    val url = URL(urlStr)

    println("Starting download...")
    val zip = File("${location}${File.separator}zip")
    val rbc = Channels.newChannel(url.openStream())
    val fos = FileOutputStream(zip)

    fos.channel.transferFrom(rbc, 0, Long.MAX_VALUE)

    fos.close()
    rbc.close()

    println("Complete download, extracting data...")

    ZipFile(zip).extractAll(location.path)

    println("Successfully extracted, moving to proper locations...")

    val unzipped = File(location.path + File.separator + getFileName())

    File(unzipped.path + File.separator + "bin" + File.separator + "ffprobe.exe")
            .renameTo(probeFile)

    File(unzipped.path + File.separator + "bin" + File.separator + "ffmpeg.exe")
            .renameTo(ffmpegFile)

    File(unzipped.path + File.separator + "bin" + File.separator + "ffplay.exe")
            .renameTo(playFile)

    println("Successfully completed FFMPEG download!")

    zip.delete()
    unzipped.deleteRecursively()

    println("Deleted excess files!")
    hasFfmpeg = true
    downloadPanel.isVisible = false
}

/**
 * Get a thumbnail from the video
 */
fun createThumbnail(): String {
    val name = "thumbnail.png"

    Executor.runJob(FFmpegBuilder()
            .setInput(inputFileName)
            .addOutput(name)
            .setFrames(1)
            .done())

    return name
}

/**
 * Combine audio & video.
 */
fun combineAudioVideo(audioFile: String) {
    Executor.ffmpeg.run(listOf("-i", inputFileName, "-i", audioFile, "-c:v", "copy", "-c:a", "aac", "combined.mp4", "-y"))
}