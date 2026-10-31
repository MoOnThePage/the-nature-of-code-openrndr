/**
 * Playground for understanding Vectors + GUI in OPENRNDR
 *
 * Controls:
 *  - Left double-click : show / hide the control panel
 *  - Right button hold + drag : move the spiral
 */
package vectors

import gui.*
import org.openrndr.MouseButton
import org.openrndr.application
import org.openrndr.color.ColorHSVa
import org.openrndr.draw.Drawer
import org.openrndr.draw.isolatedWithTarget
import org.openrndr.draw.renderTarget
import org.openrndr.math.Polar
import org.openrndr.math.Vector2
import org.openrndr.panel.ControlManager
import org.openrndr.panel.layout
import java.io.File
import kotlin.math.min

/** width/height of 0 means "use the current window size". */
data class ExportFormat(val label: String, val width: Int, val height: Int)

// Edit or add formats here, the dropdown updates automatically.
private val exportFormats = listOf(
    ExportFormat("Square 1:1 - 1080x1080", 1080, 1080),
    ExportFormat("Portrait 4:5 - 1080x1350", 1080, 1350),
    ExportFormat("Tall 9:16 - 1080x1920", 1080, 1920),
    ExportFormat("Landscape 16:9 - 1920x1080", 1920, 1080),
    ExportFormat("Landscape 16:9 - 3840x2160 (4K)", 3840, 2160),
    ExportFormat("Current window size", 0, 0),
)

// Right-button presses inside this left strip belong to the panel, not the artwork.
private const val PANEL_STRIP = 240.0

// <user home>/Pictures/BG_Exports
// Works for any user and any OS: resolve() uses the correct path separator.
private val exportDir: File = File(System.getProperty("user.home"), "Pictures")
    .resolve("BG_Exports")

fun runVectorsGui() = application {
    configure {
        width = 1080 / 2
        height = 1920 / 2
        title = "Understanding Vectors + GUI"
        windowResizable = true
    }

    program {
        val p = object {
            var count = 100
            var turnsDistance = 3.0
            var rotationDensity = 10.0
            var posX = 0.5           // 0..1 of canvas width, set by mouse
            var posY = 0.5           // 0..1 of canvas height, set by mouse
            var hue = 260.0
            var fillOpacity = 0.35
        }

        var selectedFormat = exportFormats[1]
        var pendingExport: ExportFormat? = null

        /** Draws the whole artwork for a canvas of size w x h. */
        fun drawArt(drawer: Drawer, w: Double, h: Double) {
            // Scale sizes so the look stays the same in every format
            val s = min(w, h) / min(width, height).toDouble()

            val complement = (p.hue + 180.0) % 360.0
            val background = ColorHSVa(p.hue, 0.35, 0.18).toRGBa()
            val stroke = ColorHSVa(complement, 0.35, 0.95).toRGBa()
            val fill = ColorHSVa(complement, 0.45, 0.80).toRGBa().opacify(p.fillOpacity)

            val center = Vector2(p.posX * w, p.posY * h)
            val points = List(p.count) { i ->
                center + Polar(i * p.rotationDensity, i * p.turnsDistance * s).cartesian
            }

            drawer.clear(background)
            drawer.stroke = stroke
            drawer.fill = fill
            drawer.circles(points, 10.0 * s)
        }

        val gui = ControlManager().apply {
            panelStyle()
            layout {
                intControl("Number of Points", 1, 2000, p::count)
                doubleControl("Distance between turns", 1.0, 100.0, p::turnsDistance, 1)
                doubleControl("Density of rotation", 1.0, 100.0, p::rotationDensity, 1)

                doubleControl("Color (hue)", 0.0, 360.0, p::hue, 0)
                doubleControl("Fill opacity", 0.0, 1.0, p::fillOpacity)

                dropdownControl(
                    options = exportFormats,
                    selected = selectedFormat,
                    optionLabel = { it.label }
                ) { selectedFormat = it }

                buttonControl("Export image") { pendingExport = selectedFormat }
            }
        }

        extend(gui)
        doubleClickToggle(gui)   // left double-click

        // Right button: press, hold and drag to move the spiral
        var dragging = false

        fun moveTo(pos: Vector2) {
            p.posX = (pos.x / width).coerceIn(0.0, 1.0)
            p.posY = (pos.y / height).coerceIn(0.0, 1.0)
        }

        mouse.buttonDown.listen {
            if (it.button == MouseButton.RIGHT) {
                dragging = !(gui.enabled && it.position.x < PANEL_STRIP)
                if (dragging) moveTo(it.position)
            }
        }
        mouse.dragged.listen {
            if (dragging) moveTo(it.position)
        }
        mouse.buttonUp.listen {
            if (it.button == MouseButton.RIGHT) dragging = false
        }

        extend {
            // 1. Draw to the screen
            drawArt(drawer, width.toDouble(), height.toDouble())

            // 2. Handle an export request: render offscreen at the target size
            pendingExport?.let { format ->
                pendingExport = null

                val w = if (format.width > 0) format.width else width
                val h = if (format.height > 0) format.height else height

                val target = renderTarget(w, h) {
                    colorBuffer()
                    depthBuffer()
                }

                drawer.isolatedWithTarget(target) {
                    ortho(target)
                    drawArt(this, w.toDouble(), h.toDouble())
                }

                // Safe file name (no ":" or other characters Windows rejects)
                val safeName = format.label.replace(Regex("[^A-Za-z0-9]+"), "_").trim('_')
                exportDir.mkdirs()
                val file = File(exportDir, "${safeName}_${System.currentTimeMillis()}.png")
                target.colorBuffer(0).saveToFile(file, async = false)

                target.colorBuffer(0).destroy()
                target.detachColorAttachments()
                target.destroy()
                println("Saved ${file.absolutePath}")
            }
        }
    }
}