package randomness

import org.openrndr.color.ColorHSLa
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.noise.perlin
import org.openrndr.math.Vector2
import org.openrndr.math.map

class PerlinWalker(val width: Double, val height: Double) {

    // offset in noise space
    var tx = 12.34
    var ty =  56.78

    // offset in noise space for color
    var tc = 120.0

    fun position(): Vector2 {
        // map to screen coordinates
        val x = perlin(seed = 0, x = tx).map(-1.0, 1.0, 0.0, width)
        val y = perlin(seed = 1, x = ty).map(-1.0, 1.0, 0.0, height)

        tx += 0.01
        ty += 0.01

        return Vector2(x, y)
    }

    fun color(): ColorRGBa {
        // map the noise to 360 color hues
        val hue = perlin(seed = 2, x = tc).map(-1.0, 1.0, 0.0, 360.0)

        tc += 0.005 // lower increment for smother color change

        return ColorHSLa(h= hue, s = 0.8, l = 0.5, alpha = 0.1).toRGBa()
    }
}