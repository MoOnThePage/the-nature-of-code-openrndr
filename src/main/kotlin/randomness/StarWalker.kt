package randomness

import org.openrndr.color.ColorHSLa
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.noise.perlin
import org.openrndr.math.Vector2
import org.openrndr.math.map
import kotlin.math.cos
import kotlin.math.sin

class StarWalker(val width: Double, val height: Double) {
    var angle = 0.0
    var time = 0.0

    var tc = 120.0

    fun position(): Vector2 {
        val center = Vector2(width / 2.0, height / 2.0)

        // 1. Base 5-pointed star radius modulation
        val starPoints = 5
        val baseRadius = 150.0
        val pointDepth = 60.0 // How sharp the star points are

        // Math formula for 5 peaks around a circle
        val starRadius = baseRadius + sin(starPoints * angle) * pointDepth

        // 2. Add organic noise wobble to the radius
        val noiseOffset = perlin(seed = 0, x = cos(angle), y = sin(angle), z = time) * 30.0
        val totalRadius = starRadius + noiseOffset

        // Convert polar coordinates (angle, radius) to (X, Y)
        val x = center.x + cos(angle) * totalRadius
        val y = center.y + sin(angle) * totalRadius

        // Step around the star loop
        angle += 0.02
        time += 0.005

        return Vector2(x, y)
    }

    fun color(): ColorRGBa {
        // map the noise to 360 color hues
        val hue = perlin(seed = 2, x = tc).map(-1.0, 1.0, 0.0, 360.0)

        tc += 0.005 // lower increment for smother color change

        return ColorHSLa(h= hue, s = 0.8, l = 0.5, alpha = 0.1).toRGBa()
    }
}