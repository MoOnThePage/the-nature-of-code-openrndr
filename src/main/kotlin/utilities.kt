import org.openrndr.Program
import org.openrndr.draw.BufferMultisample
import org.openrndr.ffmpeg.ScreenRecorder
import org.openrndr.ffmpeg.h264


fun Program.record(duration: Double = 26.0) {
    extend(ScreenRecorder()) {
        maximumDuration = duration // stops recording after 26 seconds
        frameRate = 60.0
        multisample = BufferMultisample.SampleCount(8) // Anti-alias sharp vector lines
        h264 {
            constantRateFactor = 10
        }
    }
}