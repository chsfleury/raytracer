package fr.chsfleury.raytracer.exercises.clock

import fr.chsfleury.raytracer.Canvas
import fr.chsfleury.raytracer.Color
import fr.chsfleury.raytracer.Pixel
import fr.chsfleury.raytracer.color
import fr.chsfleury.raytracer.linalg.Transform
import fr.chsfleury.raytracer.point
import fr.chsfleury.raytracer.swing.Window
import kotlin.math.PI
import kotlin.math.roundToInt

object Clock {

    @JvmStatic
    fun main(args: Array<String>) {
        val t = Transform.zRotation(PI / 6)
        var v = point(250, 0, 0)

        val width = 900
        val height = 600
        val canvas = Canvas(width, height)
        val color = color(1, 1, 1)

        repeat(12) {
            v = t * v
            val pixel = Pixel(
                width / 2 + v.x.roundToInt(),
                height / 2 - v.y.roundToInt()
            )

            if (pixel in canvas) {
                canvas[pixel] = color
            }
        }

        Window(canvas).show()
    }
}