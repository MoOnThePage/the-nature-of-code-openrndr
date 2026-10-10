/**
 * This is a playground for understanding Vectors GUI in OPENRDNR
 * practicing the GUI to add controls to the program
 */

// TODO: https://guide.openrndr.org/interaction/userInterfaces.html
package vectors

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.math.Polar
import org.openrndr.panel.ControlManager
import org.openrndr.panel.elements.Range
import org.openrndr.panel.elements.slider
import org.openrndr.panel.layout

// creating a spiral using polar coordinates
fun main() = application {
    configure {
        width = 1080 / 2
        height = 1920 / 2
        title = "Understanding Vectors + GUI"
        windowResizable = true
    }

    program {
        var num = 100
        extend(ControlManager()) {
            layout {
                slider {
                    label = "Number of Points"
                    range = Range(1.0, 1000.0)
                    value = 100.0
                    precision = 0
                    events.valueChanged.listen {
                        println("the new value is ${it.newValue}")
                        num = it.newValue.toInt()
                    }
                }
            }
        }

        extend {

//            val center = drawer.bounds.center
//
//            // Generate spiral starting at radius = 0, angle = 0
//            val points = List(num) { i ->
//                val radius = i * 3.0       // Controls distance between turns
//                val angle = i * 10.0       // Controls density of rotation (in degrees)
//
//                center + Polar(angle, radius).cartesian
//            }

            val points = List(num) {
                drawer.bounds.center + Polar(it * 5.0, it + 100.0).cartesian // Since drawing operations don’t accept Polar or Spherical coordinates
            }

            drawer.clear(ColorRGBa.WHITE)
            drawer.stroke = ColorRGBa.BLACK
            drawer.fill = ColorRGBa.BLACK.opacify(0.2)
            drawer.circles(points, 10.0)
        }
    }
}
