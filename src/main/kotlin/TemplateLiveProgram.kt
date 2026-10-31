//import org.openrndr.application
//import org.openrndr.color.ColorRGBa
//import org.openrndr.extra.olive.oliveProgram
//import kotlin.math.PI
//import kotlin.math.cos
//import kotlin.math.sin
//
///**
// *  This is a template for a live program.
// *
// *  It uses oliveProgram {} instead of program {}. All code inside the
// *  oliveProgram {} can be changed while the program is running.
// */
//
//fun main() = application {
//    configure {
//        width = 1080 / 2
//        height = 1920 / 2
//    }
//    oliveProgram {
//        var x = 100.0
//        var y = 100.0
//        val f = 1
//        val w = 2 * PI * f
//        var xSpeed = 24 * sin(w * seconds)
//        var ySpeed = 24 * cos(w * seconds)
//
//        extend {
//            drawer.clear(ColorRGBa.WHITE)
//
//            // update position
//            x += xSpeed
//            y += ySpeed
//
//            // bounce on borders
//            if (x > width || x < 0) {
//                xSpeed *= -1.0
//            }
//
//            if (y > height || y < 0) {
//                ySpeed *= -1.0
//            }
//
//            // Draw the ball
//            drawer.stroke = ColorRGBa.BLACK
//            drawer.circle(x, y, 16.0)
//        }
//    }
//}