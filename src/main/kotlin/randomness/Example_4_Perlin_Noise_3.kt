// Perlin Noise Star Walker
// radius = baseRadius + sin(5 x theta) x amplitude + noise
package randomness

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.isolatedWithTarget
import org.openrndr.draw.renderTarget
import record

fun main() = application {
    configure {
        width = 1080 / 2
        height = 1920 / 2
        title = "Perlin Noise Walker"
    }

    program {
        val walker = StarWalker(width.toDouble(), height.toDouble())

        // Create an offscreen buffer matching the window size
        val target = renderTarget(width, height) {
            colorBuffer()
        }

        val bgColor = ColorRGBa.BLACK

        // Initialize the background to white
        drawer.isolatedWithTarget(target) {
            clear(bgColor)
        }

//        record() // record 26 seconds by default

        extend {
            val walkerColor = walker.color()
            val position = walker.position() //  draw smooth wanderer position

            // step and draw the walker into the offscreen buffer
            drawer.isolatedWithTarget(target) {
                drawer.fill = walkerColor.opacify(0.1)
                drawer.stroke = walkerColor
                drawer.circle(position, 24.0)
            }

            // Draw the offscreen buffer onto the screen
            drawer.image(target.colorBuffer(0))
        }
    }
}
