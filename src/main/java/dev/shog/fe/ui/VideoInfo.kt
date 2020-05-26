package dev.shog.fe.ui

import dev.shog.fe.Executor.ffprobe
import dev.shog.fe.checkExists
import dev.shog.fe.hasFfmpeg
import dev.shog.fe.inputFileName
import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.WindowConstants

object VideoInfo : JPanel() {
    private val frameCount: JLabel = JLabel("Frame Count:")
    private val aspectRatio: JLabel = JLabel("Aspect Ratio:")
    private val heightWidth: JLabel = JLabel("Width/Height:")
    private val frameRate: JLabel = JLabel("Frame Rate:")
    private val codecName: JLabel = JLabel("Codec:")
    private val length: JLabel = JLabel("Length:")

    /**
     * Update & create a JFrame
     */
    fun update() {
        if (hasFfmpeg && checkExists()) {
            val probe = ffprobe.probe(inputFileName).getStreams().first()

            frameCount.text = "Frame Count: ${probe.nb_frames}"
            aspectRatio.text = "Aspect Ratio: ${probe.display_aspect_ratio}"
            heightWidth.text = "Width/Height: ${probe.width}/${probe.height}"
            frameRate.text = "Frame Rate: ${probe.avg_frame_rate} or ${probe.avg_frame_rate.properWhole}"
            codecName.text = "Codec: ${probe.codec_name}"
            length.text = "Length: ${probe.nb_frames / probe.avg_frame_rate.properWhole} seconds"

            val frame = JFrame()

            frame.contentPane.add(this)
            frame.isVisible = true
            frame.defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE

            frame.pack()
        }
    }

    init {
        preferredSize = Dimension(538, 152)
        layout = null

        add(frameCount)
        add(aspectRatio)
        add(heightWidth)
        add(frameRate)
        add(codecName)
        add(length)

        frameCount.setBounds(5, 10, 290, 25)
        aspectRatio.setBounds(5, 30, 225, 25)
        heightWidth.setBounds(5, 50, 195, 25)
        frameRate.setBounds(5, 70, 250, 25)
        codecName.setBounds(5, 90, 145, 25)
        length.setBounds(5, 110, 100, 25)
    }
}
