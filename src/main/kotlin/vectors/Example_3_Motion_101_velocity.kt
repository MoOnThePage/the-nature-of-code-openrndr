package vectors

import org.openrndr.WindowMultisample
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.loadFont
import org.openrndr.math.Vector2
import record
import kotlin.random.Random

// ---------------------------------------------------------------------------
// SETTINGS
// How many circles to create. Change this one number to add or remove movers.
// ---------------------------------------------------------------------------
const val MOVER_COUNT = 2000

// ---------------------------------------------------------------------------
// PALETTE
// Each mover picks one color from this list when it is created.
// Add more colors to the list and they will be used automatically.
// ---------------------------------------------------------------------------
val MOVER_COLORS = listOf(
    ColorRGBa.fromHex("#ff007f"),   // neon pink (your original)
    ColorRGBa.fromHex("#00ffcc"),   // neon cyan (same as your text)
)
val BACKGROUND = ColorRGBa.fromHex("#111111")

// ---------------------------------------------------------------------------
// MOVER
// Every Mover object stores its OWN location, velocity, radius and color.
// That is the whole trick: one class, many independent instances.
//
// radius and color are now constructor parameters, so each circle can differ.
// ---------------------------------------------------------------------------
class Mover(
    private val boundsWidth: Double,
    private val boundsHeight: Double,
    val radius: Double,
    val color: ColorRGBa
) {

    // Random starting position anywhere inside the window.
    // Random.nextDouble(from, until) returns a Double in [from, until).
    var location = Vector2(
        Random.nextDouble(0.0, boundsWidth),
        Random.nextDouble(0.0, boundsHeight)
    )

    // Velocity in PIXELS PER SECOND, so speed is independent of frame rate.
    // Each mover gets its own random velocity, so they all drift differently.
    var velocity = Vector2(
        Random.nextDouble(-300.0, 300.0),
        Random.nextDouble(-300.0, 300.0)
    )

    // distance = speed * time. `dt` is the seconds since the previous frame.
    fun update(dt: Double) {
        location += velocity * dt
    }

    // Wrap-around edges. Vector2 is immutable, so we build a new one with
    // .copy(), changing only the component we need.
    fun checkEdges() {
        if (location.x > boundsWidth) {
            location = location.copy(x = 0.0)
        } else if (location.x < 0.0) {
            location = location.copy(x = boundsWidth)
        }

        if (location.y > boundsHeight) {
            location = location.copy(y = 0.0)
        } else if (location.y < 0.0) {
            location = location.copy(y = boundsHeight)
        }
    }

    // Draws this one circle using its own color and radius.
    fun display(drawer: Drawer) {
        drawer.stroke = color
        drawer.fill = color.opacify(0.1)   // translucent fill, solid outline
        drawer.circle(location, radius)
    }
}

// ---------------------------------------------------------------------------
// PROGRAM
// ---------------------------------------------------------------------------
fun main() = application {
    configure {
        width = 1080 / 2
        height = 1920 / 2
        multisample = WindowMultisample.SampleCount(4)
    }

    program {
        val font = loadFont("data/fonts/default.otf", 30.0)

        // setup(): build the whole group of movers once.
        // List(n) { ... } runs the block n times and collects the results.
        // Each run creates a NEW Mover with its own random radius and color.
        val movers = List(MOVER_COUNT) {
            Mover(
                boundsWidth = width.toDouble(),
                boundsHeight = height.toDouble(),
                radius = Random.nextDouble(10.0, 35.0),
                color = MOVER_COLORS.random()   // pick a random palette color
            )
        }

        // Uncomment to record (must be called before extend { }).
        record(10.0)

        var lastSeconds = 0.0

        extend {
            val dt = seconds - lastSeconds
            lastSeconds = seconds

            drawer.clear(BACKGROUND)

            // The same three steps as before, now applied to every mover.
            // Updating all movers first and drawing after keeps logic and
            // drawing cleanly separated.
            for (mover in movers) {
                mover.update(dt)       // 1. move
                mover.checkEdges()     // 2. wrap around the window edges
                mover.display(drawer)  // 3. draw
            }

            // Typography
            drawer.fontMap = font
            drawer.fill = ColorRGBa.fromHex("#00ffcc")
            drawer.text(
                "Motion 101 (velocity)".uppercase(),
                10.0,
                40.0
            )
        }
    }
}