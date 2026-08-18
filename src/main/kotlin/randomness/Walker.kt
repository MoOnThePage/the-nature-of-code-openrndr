/**
 * The Walker class in OPENRNDR
 * @author Mohammad Ahmad ALhamad
 * @version 1.0
 * @since 2026-08-18
 *
 * Description:
 * - Use Kotlin Vector2 property to store coordinates
 * - Kotlin uses Random.nextDouble for randomness.
 * - Daw directly onto a Drawer object.
 **/
package randomness

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.math.Vector2
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class Walker(var position: Vector2) {
    val stepSize = 20.0

    // walking
    fun walk() {
        val angle = Random.nextDouble(0.0, 2.0 * PI) // random angle for direction in radians
        val walkDirection = Vector2(
            x = cos(angle) * stepSize,
            y = sin(angle) * stepSize
        )
        position += walkDirection
    }

    // render the walker
    fun draw(drawer: Drawer) {
        val walkerColor = ColorRGBa.fromHex("#0D3A32")
        // set the fill color
        drawer.fill = walkerColor

        // draw the circle
        drawer.circle(position, 4.0) // 4px radius = 8px diameter
    }
}