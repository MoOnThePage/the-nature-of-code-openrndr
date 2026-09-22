// Tree animated by Spatial Perlin Air Flow
package randomness

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated
import org.openrndr.extra.noise.perlin
import org.openrndr.math.Vector2
import org.openrndr.math.map
import record
import kotlin.math.max

fun main() = application {
    configure {
        width = 1080 / 2
        height = 1920 / 2
        title = "Tree with Perlin Air Flow"
    }
    program {

        record() // record 26 seconds by default
        extend {
            drawer.clear(ColorRGBa.BLACK)
            drawer.stroke = ColorRGBa.YELLOW
            drawer.strokeWeight = 1.5

            val rootWorldPos = Vector2(width / 2.0, height.toDouble() - 20.0)

            drawer.isolated {
                drawer.translate(rootWorldPos)

                branch(
                    length = height * 0.20,
                    thickness = 4.0,
                    noiseOffset = 0.0,
                    worldPos = rootWorldPos,
                    time = seconds * 0.3, // Wind field progression speed
                    seed = 42
                )
            }
        }
    }
}

fun Drawer.branch(
    length: Double,
    thickness: Double,
    noiseOffset: Double,
    worldPos: Vector2,
    time: Double,
    seed: Int
) {
    if (length < 4.0) return

    strokeWeight = max(0.5, thickness)

    // Calculate air flow vector at this current branch's location in space
    // Spatial scaling factor (0.003) controls how wide gust boundaries are across the screen
    val spatialX = worldPos.x * 0.003
    val spatialY = worldPos.y * 0.003

    // Evaluate 2D/3D Perlin noise at the absolute world position + time
    val windForce = perlin(seed = 99, x = spatialX + time, y = spatialY)

    // Map noise [-1.0, 1.0] to a bending angle offset (e.g., -15 degrees to +15 degrees torque)
    // Thinner/higher branches bend more easily under wind pressure
    val windBend = windForce.map(-1.0, 1.0, -15.0, 15.0) * (120.0 / length)

    // Apply the local wind torque to the current segment before drawing
    isolated {
        rotate(windBend)
        lineSegment(Vector2.ZERO, Vector2(0.0, -length))

        translate(0.0, -length)

        // Compute local branch tip location in global screen coordinates
        // Using approximate vector math relative to the unrotated system
        val nextWorldPos = worldPos + Vector2(0.0, -length)

        // Base structural branch angles (static natural growth topology)
        val baseAngleLeft = perlin(seed, noiseOffset).map(-1.0, 1.0, 12.0, 35.0)
        val baseAngleRight = perlin(seed + 1, noiseOffset + 100.0).map(-1.0, 1.0, 12.0, 35.0)

        val shrinkLeft = perlin(seed + 2, noiseOffset * 0.5).map(-1.0, 1.0, 0.65, 0.75)
        val shrinkRight = perlin(seed + 3, noiseOffset * 0.5 + 50.0).map(-1.0, 1.0, 0.65, 0.75)

        // --- Left Branch ---
        isolated {
            rotate(-baseAngleLeft)
            branch(
                length = length * shrinkLeft,
                thickness = thickness * 0.7,
                noiseOffset = noiseOffset + 0.15,
                worldPos = nextWorldPos,
                time = time,
                seed = seed
            )
        }

        // --- Right Branch ---
        isolated {
            rotate(baseAngleRight)
            branch(
                length = length * shrinkRight,
                thickness = thickness * 0.7,
                noiseOffset = noiseOffset + 0.15,
                worldPos = nextWorldPos,
                time = time,
                seed = seed + 10
            )
        }
    }
}