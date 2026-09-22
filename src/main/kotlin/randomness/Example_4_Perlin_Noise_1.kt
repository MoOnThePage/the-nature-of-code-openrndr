// 1D Noise & Time Step (t)
package randomness

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.color.presets.DARK_BLUE
import org.openrndr.extra.noise.perlin
import org.openrndr.math.map
import record

fun main() = application {
    configure {
        width = 1080 / 2
        height = 1920 / 2
        title = "Perlin Noise 1D"
    }

    program {
        var t = 0.0 // initial time step

//        record(10.0) // record 26 seconds by default

        extend {
            drawer.clear(ColorRGBa.WHITE)

            val n = perlin(0, t) // get perlin noise at time t
            val x = n.map(-2.0, 2.0, 0.0, width.toDouble()) // map to screen coordinates

            drawer.fill = null
            drawer.stroke = ColorRGBa.DARK_BLUE
            drawer.strokeWeight = 2.0
            drawer.circle(x, height / 2.0, 30.0)

            t += 0.01
        }
    }
}