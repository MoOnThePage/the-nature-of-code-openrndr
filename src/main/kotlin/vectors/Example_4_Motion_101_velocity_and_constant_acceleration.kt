package vectors

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.loadFont
import org.openrndr.math.Vector2
import record

// ---------------------------------------------------------------------------
// PALETTE
// Defined once at the top so every color in the sketch comes from one place.
// fromHex() takes a hex string (no '#') and returns a ColorRGBa.
// ---------------------------------------------------------------------------
val RED: ColorRGBa = ColorRGBa.fromHex("E4312B")
val BLACK: ColorRGBa = ColorRGBa.fromHex("000000")
val WHITE: ColorRGBa = ColorRGBa.fromHex("FFFFFF")
val GREEN: ColorRGBa = ColorRGBa.fromHex("149954")

// ---------------------------------------------------------------------------
// MOVER
// Processing's PVector is MUTABLE (add() changes the vector in place).
// OPENRNDR's Vector2 is IMMUTABLE (operators return a NEW vector), so we
// declare the fields as `var` and reassign them: velocity = velocity + acceleration
// ---------------------------------------------------------------------------
class Mover_1(width: Int, height: Int) {

    // Where the mover is. Starts at the center of the window (like width/2, height/2).
    // Note the .toDouble(): OPENRNDR uses Double for vectors, Processing used float.
    var location = Vector2(width / 2.0, height / 2.0)

    // How fast (and in which direction) the mover is moving, in pixels per frame.
    var velocity = Vector2.ZERO

    // How the velocity changes each frame.
    // x = -0.001 -> a tiny pull to the left
    // y =  0.01  -> a stronger pull downward (the y axis points DOWN on screen)
    var acceleration = Vector2(-0.001, 0.01)

    // The maximum speed (length of the velocity vector) the mover may reach.
    val topspeed = 10.0

    // Recent positions, used only to draw a fading trail (not in the original).
    private val trail = ArrayDeque<Vector2>()
    private val trailLength = 60

    // -----------------------------------------------------------------------
    // update(): the physics step, called once per frame.
    // -----------------------------------------------------------------------
    fun update() {
        // 1. Acceleration changes velocity.
        velocity += acceleration

        // 2. Limit the velocity. OPENRNDR's Vector2 has no limit() like PVector,
        //    so we do it by hand: if the vector is too long, shrink it to topspeed
        //    while keeping its direction (normalized = length 1, then scale).
        if (velocity.length > topspeed) {
            velocity = velocity.normalized * topspeed
        }

        // 3. Velocity changes location.
        location += velocity

        // Remember where we've been, dropping the oldest point when the trail is full.
        trail.addLast(location)
        if (trail.size > trailLength) trail.removeFirst()
    }

    // -----------------------------------------------------------------------
    // checkEdges(): wrap around, so leaving one side re-enters from the other.
    // -----------------------------------------------------------------------
    fun checkEdges(width: Int, height: Int) {
        var x = location.x
        var y = location.y
        var wrapped = false

        if (x > width) { x = 0.0; wrapped = true }
        else if (x < 0) { x = width.toDouble(); wrapped = true }

        if (y > height) { y = 0.0; wrapped = true }
        else if (y < 0) { y = height.toDouble(); wrapped = true }

        location = Vector2(x, y)

        // Clear the trail after a wrap so we don't draw dots across the jump.
        if (wrapped) trail.clear()
    }

    // -----------------------------------------------------------------------
    // display(): draws the mover. In OPENRNDR drawing needs the Drawer, so we
    // receive it as a parameter instead of using global drawing functions.
    // -----------------------------------------------------------------------
    fun display(drawer: Drawer) {
        drawer.stroke = null // no outlines, only filled shapes

        // Trail: older points are smaller and more transparent (red).
        trail.forEachIndexed { i, p ->
            val t = (i + 1) / trail.size.toDouble() // 0 (oldest) -> 1 (newest)
            drawer.fill = RED.opacify(t * 0.6)
            drawer.circle(p, 2.0 + t * 6.0)
        }

        // Neon glow: stacked translucent green circles, big and faint to small.
        for (layer in 4 downTo 1) {
            drawer.fill = GREEN.opacify(0.12)
            drawer.circle(location, 8.0 + layer * 7.0)
        }

        // Bright core: a solid white circle with a red ring around it.
        drawer.fill = WHITE
        drawer.stroke = RED
        drawer.strokeWeight = 2.0
        drawer.circle(location, 8.0)
    }
}

// ---------------------------------------------------------------------------
// PROGRAM
// application { } is the entry point (like Processing's setup() + draw()).
// ---------------------------------------------------------------------------
fun main() = application {
    configure {
        width = 1080 / 2
        height = 1920 / 2
    }

    // program { } runs once, like setup(). Anything declared here lives for the
    // whole run, which is where we create our Mover.
    program {
        val font = loadFont("data/fonts/default.otf", 25.0)
        val mover = Mover_1(width, height)

        // Uncomment to record (must be called before extend { }).
        record()

        // extend { } runs every frame, like draw().
        extend {
            drawer.clear(BLACK)       // wipe the previous frame
            mover.update()            // physics
            mover.checkEdges(width, height)
            mover.display(drawer)     // render

            // Typography
            drawer.fontMap = font
            drawer.fill = ColorRGBa.fromHex("#00ffcc")
            drawer.text(
                "Motion 101 Velocity & Constant Acceleration".uppercase(),
                10.0,
                40.0
            )
        }
    }
}