package randomness

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import record
import kotlin.random.Random

fun main() = application {
    configure {
        width = 1080 / 2
        height = 1920 / 2
        title = "Random Number Distribution"
    }

    program {
        val bgColor = ColorRGBa.fromHex("#490054")
        val lineColor = ColorRGBa.fromHex("#720137")
        val barColor = ColorRGBa.fromHex("#46000D")

        val randomCounts = IntArray(10)

//        record(10.0) // record 26 second by default
        extend {
            drawer.clear(bgColor)

            // pick a random number and increment count
            val index = Random.nextInt(randomCounts.size)
            randomCounts[index]++

            // render bars
            val w = width.toDouble() / randomCounts.size
            drawer.fill = barColor
            drawer.stroke = lineColor

            for (x in randomCounts.indices) {
                val h = randomCounts[x].toDouble() * 20 // times 20 to speed it up
                drawer.rectangle(
                    x = x * w,
                    y = height - h,
                    width = w - 1.0,
                    height = h
                )
            }
        }
    }
}