package randomness

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.isolatedWithTarget
import org.openrndr.draw.renderTarget
import org.openrndr.math.Vector2
import record

fun main() = application {
    configure {
        width = 1080 / 2
        height = 1920 / 2
        title = "Traditional Random Walk"
    }

    program {
        // Initialize Walker in the center of the window
        val walker = Walker(Vector2(width / 2.0, height / 2.0))

        // Create an offscreen buffer matching the window size
        val target = renderTarget(width, height) {
            colorBuffer()
        }

        val bgColor = ColorRGBa.fromHex("#192A3C")

        // Initialize the background to white
        drawer.isolatedWithTarget(target) {
            clear(bgColor)
        }

//        record() // record 26 second by default
        extend {
            // step and draw the walker into the offscreen buffer
            drawer.isolatedWithTarget(target) {
                walker.walk()
                walker.draw(drawer)
            }

            // Draw the offscreen buffer onto the screen
            drawer.image(target.colorBuffer(0))
        }
    }
}