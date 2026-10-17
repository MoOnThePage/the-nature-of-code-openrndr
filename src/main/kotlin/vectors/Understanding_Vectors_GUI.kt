/**
 * This is a playground for understanding Vectors GUI in OPENRDNR
 * practicing the GUI to add controls to the program
 */

// TODO: Add color Controls to the GUI
// TODO: try to understand how to change the layout https://guide.openrndr.org/interaction/userInterfaces.html
package vectors

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.extensions.Screenshots
import org.openrndr.math.Polar
import org.openrndr.math.Vector2
import org.openrndr.panel.ControlManager
import org.openrndr.panel.elements.Range
import org.openrndr.panel.elements.button
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
        var turnsDistance = 3.0 // Controls distance between turns
        var rotationDensity = 10.0 // Controls density of rotation (in degrees)

        // Initial center position variables
        var centerX = width / 2.0
        var centerY = height / 2.0

        // instantiate the screenshot extension to reference it
        val screenshots = Screenshots()

        extend(ControlManager()) {
            layout {
                slider {
                    label = "Number of Points"
                    range = Range(1.0, 2000.0)
                    value = 100.0
                    precision = 0
                    events.valueChanged.listen {
                        num = it.newValue.toInt()
                    }
                }

                slider {
                    label = "Distance between turns"
                    range = Range(1.0, 100.0)
                    value = 3.0
                    precision = 0
                    events.valueChanged.listen {
                        turnsDistance = it.newValue
                    }
                }

                slider {
                    label = "Density of rotation"
                    range = Range(1.0, 100.0)
                    value = 10.0
                    precision = 0
                    events.valueChanged.listen {
                        rotationDensity = it.newValue
                    }
                }

                slider {
                    label = "X Position"
                    range = Range(0.0, width.toDouble())
                    value = centerX
                    precision = 1
                    events.valueChanged.listen {
                        centerX = it.newValue
                    }
                }

                slider {
                    label = "Y Position"
                    range = Range(0.0, height.toDouble())
                    value = centerY
                    precision = 1
                    events.valueChanged.listen {
                        centerY = it.newValue
                    }
                }

                button{
                    label = "Take Screenshot"
                    events.clicked.listen {
                        screenshots.trigger()
                    }
                }
            }
        }

        extend(screenshots)
        extend {

            val center = Vector2(centerX, centerY)

            // Generate spiral starting at radius = 0, angle = 0
            val points = List(num) { i ->
                val radius = i * turnsDistance
                val angle = i * rotationDensity

                center + Polar(angle, radius).cartesian
            }

            drawer.clear(ColorRGBa.WHITE)
            drawer.stroke = ColorRGBa.BLACK
            drawer.fill = ColorRGBa.BLACK.opacify(0.2)
            drawer.circles(points, 10.0)
        }
    }
}
