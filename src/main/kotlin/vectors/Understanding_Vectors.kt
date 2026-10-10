// This is a playground for understanding Vectors in OPENRDNR
package vectors

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.extensions.Screenshots
import org.openrndr.math.Polar

// creating a spiral using polar coordinates
fun main() = application {
    configure {
        width = 1080 / 2
        height = 1920 / 2
        title = "Understanding Vectors"
        windowResizable = true
    }

    program {
        val points = List(1000) {
            drawer.bounds.center + Polar(it * 5.0, it + 100.0).cartesian // Since drawing operations don’t accept Polar or Spherical coordinates
        }

        extend(Screenshots())
        extend {
            drawer.clear(ColorRGBa.WHITE)
            drawer.stroke = ColorRGBa.BLACK
            drawer.fill = ColorRGBa.BLACK.opacify(0.2)
            drawer.circles(points, 10.0)
        }
    }
}
