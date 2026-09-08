package randomness

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.math.Vector2
import record
import java.util.Random

fun main() = application {
    configure {
        width = 1080 / 2
        height = 1920 / 2
        title = "Gaussian Distribution"
    }

    program {
        val generator = Random()
        val points = mutableListOf<Vector2>()

        record() // record 26 second by default
        extend {
            val gaussianValue = generator.nextGaussian().toFloat()
            val standardDeviation = 60.0f
            val mean = width / 2.0f

            val value = (standardDeviation * gaussianValue) + mean

            points.add(Vector2(value.toDouble(), height / 2.0))

            drawer.clear(ColorRGBa.fromHex("#192A3C"))
            drawer.stroke = null
            drawer.fill = ColorRGBa.RED.copy(alpha = 0.08)
            drawer.circles(points, 4.0)
        }
    }
}