package dev.shog.fe

import dev.shog.fe.ui.DownloadWait
import dev.shog.fe.ui.MainPanel
import java.io.File
import javax.swing.JFrame
import javax.swing.UIManager
import javax.swing.WindowConstants

/**
 * The input file name
 */
var inputFileName = ""

/**
 * If FFMPEG has been downloaded.
 */
var hasFfmpeg = false

/**
 * If [inputFileName] exists.
 */
fun checkExists(): Boolean =
        File(inputFileName).exists()

/**
 * Create the main panel.
 */
fun createPanel() {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())

    val frame = JFrame("FilmEditor")

    frame.contentPane.add(MainPanel)
    frame.isVisible = true
    frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE

    frame.pack()
}

/**
 * The download panel.
 */
val downloadPanel: JFrame by lazy {
    val frame = JFrame("Downloading...")

    frame.contentPane.add(DownloadWait)

    frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE

    frame.pack()

    frame
}

fun main() {
    createPanel()
    downloadDependencies()
}