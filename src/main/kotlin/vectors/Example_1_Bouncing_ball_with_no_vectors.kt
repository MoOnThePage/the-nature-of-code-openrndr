package vectors

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.color.presets.DARK_BLUE
import org.openrndr.extra.color.presets.DARK_GREEN
import org.openrndr.extra.color.presets.DARK_OLIVE_GREEN
import org.openrndr.extra.color.presets.DARK_ORANGE
import org.openrndr.extra.color.presets.DARK_TURQUOISE
import record


fun main() = application {
    configure {
        width = 1080 / 2
        height = 1080 / 2
    }

    program {
        var x = 100.0
        var y = 100.0
        var xSpeed = 4.0
        var ySpeed = 3.0
        val radius = 16.0

        record()
        extend {
            drawer.clear(ColorRGBa.DARK_OLIVE_GREEN)

            // update position
            x += xSpeed
            y += ySpeed

            // bounce on borders
            if (x + radius > width) {
                x = width - radius
                xSpeed *= -1.0
            } else if (x - radius < 0) {
                x = radius
                xSpeed *= -1.0
            }

            if (y + radius > height) {
                y = height - radius
                ySpeed *= -1.0
            } else if (y - radius < 0) {
                y = radius
                ySpeed *= -1.0
            }

            // Draw the ball
            drawer.stroke = ColorRGBa.BLACK
            drawer.fill = ColorRGBa.DARK_ORANGE.opacify(0.5)
            drawer.circle(x, y, 16.0)
        }
    }
}