package dev.shog.fe.ui

import java.awt.Dimension
import java.util.*
import javax.swing.JLabel
import javax.swing.JPanel
import kotlin.concurrent.timerTask

/**
 * The waiting screen while FFMPEG is being downloaded.
 */
object DownloadWait : JPanel() {
    private val label: JLabel = JLabel("FFMPEG is currently being downloaded...")
    val timer = Timer()

    init {
        preferredSize = Dimension(282, 58)
        layout = null

        add(label)

        label.setBounds(30, 10, 230, 25)

        timer.schedule(timerTask {
            if (label.text.endsWith("..."))
                label.text = label.text.replace("...", ".")
            else label.text += "."
        }, 800, 800)
    }
}
