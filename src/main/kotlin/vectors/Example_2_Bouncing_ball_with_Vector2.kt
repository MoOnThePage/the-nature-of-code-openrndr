package vectors

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.loadFont
import org.openrndr.math.Vector2
import record

fun main() = application {
    configure {
        width = 1080 / 2
        height = 1920 / 2
    }

    program {
        val font = loadFont("data/fonts/default.otf", 30.0)
        val xSpeed = 4.0
        val ySpeed = 3.0
        val radius = 16.0

        // Define position and velocity as Vector2 instances
        var position = Vector2(width / 2.0, height / 2.0)
        var velocity = Vector2(xSpeed, ySpeed)

//        record()
        extend {
            drawer.clear(ColorRGBa.fromHex("#111111"))

            // update the positon
            position += velocity

            // bound the ball
            if ((position.x + radius) > width || (position.x - radius) < 0) {
                velocity = velocity.copy(x = -velocity.x)
            }

            if ((position.y + radius) > height || (position.y - radius) < 0) {
                velocity = velocity.copy(y = -velocity.y)
            }

            // Draw the ball
            drawer.stroke = ColorRGBa.fromHex("#ff007f")
            drawer.fill = ColorRGBa.fromHex("#ff007f").opacify(0.1)
            drawer.circle(position, radius)

            // Typography
            drawer.fontMap = font
            drawer.fill = ColorRGBa.fromHex("#00ffcc")
            drawer.text(
                "bouncing ball with vector2".uppercase(),
                10.0,
                40.0
            )
        }
    }
}