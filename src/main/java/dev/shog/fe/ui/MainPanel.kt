package dev.shog.fe.ui

import dev.shog.fe.*
import java.awt.Dimension
import java.io.File
import javax.swing.*

/**
 * Main display panel with various functions.
 */
object MainPanel : JPanel() {
    private val title: JLabel = JLabel("Film Editor")
    private val subtitle: JLabel = JLabel("by AJ")
    private val functions: JLabel = JLabel("Functions:")
    private val inputLabel: JLabel = JLabel("Input File (${inputFileName.ifBlank { "None" }})")
    private val applyInput: JButton = JButton("Apply")
    private val randomThumb: JButton = JButton("Get a Thumbnail")
    private val inputFile: JTextField = JTextField(5)
    private val inputFileNameStatus: JLabel = JLabel("")
    private val rthumbStatus: JLabel = JLabel("")
    private val viewData: JButton = JButton("View Video Data")
    private val videoDataStatus: JLabel = JLabel("")
    private val combineAudio: JButton = JButton("Combine Video with Audio")
    private val audioFile: JTextField = JTextField("input.mp3", 5)
    private val combineAudioStatus: JLabel = JLabel("")

    init {
        preferredSize = Dimension(600, 250)
        layout = null

        add(title)
        add(subtitle)
        add(functions)
        add(inputLabel)
        add(applyInput)
        add(randomThumb)
        add(inputFile)
        add(inputFileNameStatus)
        add(rthumbStatus)
        add(viewData)
        add(videoDataStatus)
        add(combineAudio)
        add(audioFile)
        add(combineAudioStatus)

        title.setBounds(20, 15, 100, 25)
        subtitle.setBounds(20, 30, 100, 25)
        functions.setBounds(20, 110, 100, 25)
        inputLabel.setBounds(215, 10, 200, 25)
        applyInput.setBounds(215, 65, 100, 25)
        randomThumb.setBounds(20, 140, 175, 30)
        inputFile.setBounds(215, 35, 100, 25)
        inputFileNameStatus.setBounds(215, 95, 250, 25)
        rthumbStatus.setBounds(200, 140, 500, 25)
        viewData.setBounds(20, 175, 175, 30)
        videoDataStatus.setBounds(200, 175, 100, 25)

        combineAudio.setBounds(20, 210, 175, 30)
        audioFile.setBounds(200, 210, 100, 30)
        combineAudioStatus.setBounds(315, 210, 250, 25)

        applyInput.addActionListener {
            if (File(inputFile.text).exists()) {
                inputFileNameStatus.text = "Successful!"
                inputFileName = inputFile.text
                inputLabel.text = "Input File (${inputFileName.ifBlank { "None" }})"
            } else {
                inputFileNameStatus.text = "Failed to find file!"
            }
        }

        viewData.addActionListener {
            if (checkExists()) {
                if (hasFfmpeg) {
                    VideoInfo.update()
                } else {
                    rthumbStatus.text = "Please wait for FFMPEG to complete download!"
                }
            } else rthumbStatus.text = "Invalid input file!"
        }

        randomThumb.addActionListener {
            if (checkExists()) {
                if (hasFfmpeg) {
                    createThumbnail()
                    rthumbStatus.text = "A thumbnail has been created!"
                } else {
                    rthumbStatus.text = "Please wait for FFMPEG to complete download!"
                }
            } else rthumbStatus.text = "Invalid input file!"
        }

        combineAudio.addActionListener {
            if (checkExists() && File(audioFile.text).exists()) {
                if (hasFfmpeg) {
                    combineAudioVideo(audioFile.text)
                    combineAudioStatus.text = "Audio and video has been combined!"
                } else {
                    combineAudioStatus.text = "Please wait for FFMPEG to complete download!"
                }
            } else combineAudioStatus.text = "Invalid input file(s)!"
        }
    }
}

