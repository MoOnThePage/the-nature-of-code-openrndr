package gui

import org.openrndr.MouseButton
import org.openrndr.Program
import org.openrndr.color.ColorRGBa
import org.openrndr.panel.ControlManager
import org.openrndr.panel.elements.*
import org.openrndr.panel.style.*
import org.openrndr.panel.styleSheet
import kotlin.math.roundToInt
import kotlin.reflect.KMutableProperty0

fun Element.doubleControl(
    name: String, min: Double, max: Double,
    prop: KMutableProperty0<Double>, decimals: Int = 2
) = slider {
    label = name
    range = Range(min, max)
    value = prop.get()
    precision = decimals
    events.valueChanged.listen { prop.set(it.newValue) }
}

fun Element.intControl(
    name: String, min: Int, max: Int, prop: KMutableProperty0<Int>
) = slider {
    label = name
    range = Range(min.toDouble(), max.toDouble())
    value = prop.get().toDouble()
    precision = 0
    events.valueChanged.listen { prop.set(it.newValue.roundToInt()) }
}

fun Element.boolControl(
    name: String, prop: KMutableProperty0<Boolean>
) = toggle {
    label = name
    value = prop.get()
    events.valueChanged.listen { prop.set(it.newValue) }
}

fun Element.buttonControl(name: String, onClick: () -> Unit) = button {
    label = name
    events.clicked.listen { onClick() }
}

/**
 * Dropdown menu. The button shows the currently selected option.
 * No separate label is set, because the dropdown draws label and value
 * on top of each other. optionLabel turns each option into its list text.
 */
fun <T> Element.dropdownControl(
    options: List<T>,
    selected: T,
    optionLabel: (T) -> String = { it.toString() },
    onPick: (T) -> Unit
) = dropdownButton {
    label = ""
    options.forEach { option ->
        val entry = item {
            label = optionLabel(option)
            events.picked.listen { onPick(option) }
        }
        if (option == selected) value = entry
    }
}

/** Pink panel pinned to the top-left. Call inside ControlManager().apply { ... } */
fun ControlManager.panelStyle(left: Double = 20.0, top: Double = 20.0) {
    styleSheet(has type "slider") {
        color = Color.RGBa(ColorRGBa.PINK)
        width = 200.px
    }
    styleSheet(has type "button") {
        background = Color.RGBa(ColorRGBa.PINK)
        color = Color.RGBa(ColorRGBa.BLACK)
        width = 200.px
    }
    styleSheet(has type "dropdown-button") {
        background = Color.RGBa(ColorRGBa.PINK)
        color = Color.RGBa(ColorRGBa.BLACK)
        width = 200.px
    }
    styleSheet(has type "x-root") {
        position = Position.ABSOLUTE
        this.left = left.px
        this.top = top.px
    }
}

/** Left double-click anywhere to show/hide the panel. */
fun Program.doubleClickToggle(panel: ControlManager, threshold: Double = 0.25) {
    var lastClick = Double.NEGATIVE_INFINITY
    mouse.buttonUp.listen {
        if (it.button != MouseButton.LEFT) return@listen
        if (seconds - lastClick < threshold) {
            panel.enabled = !panel.enabled
            lastClick = Double.NEGATIVE_INFINITY
        } else {
            lastClick = seconds
        }
    }
}