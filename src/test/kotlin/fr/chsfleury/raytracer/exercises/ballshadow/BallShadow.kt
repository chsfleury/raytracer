package fr.chsfleury.raytracer.exercises.ballshadow

import fr.chsfleury.raytracer.Canvas
import fr.chsfleury.raytracer.Color
import fr.chsfleury.raytracer.Intersection.Companion.hit
import fr.chsfleury.raytracer.color
import fr.chsfleury.raytracer.point
import fr.chsfleury.raytracer.ray
import fr.chsfleury.raytracer.rotationY
import fr.chsfleury.raytracer.rotationZ
import fr.chsfleury.raytracer.scaling
import fr.chsfleury.raytracer.shearing
import fr.chsfleury.raytracer.sphere
import fr.chsfleury.raytracer.swing.Window
import kotlin.math.PI
import kotlin.math.min

object BallShadow {

    @JvmStatic
    fun main(args: Array<String>) {
        val width = 400
        val height = 600

        val canvas = Canvas(width, height)
        val color = color(1, 0, 0)


        val s = sphere(transform = shearing(1, 0, 0, 0, 0, 0) * scaling(0.5, 1, 1))
        val light = point(0, 0, -5)
        val wallZ = 10.0
        val worldSquareSize = 7.0
        val pixelSize = worldSquareSize / min(width, height)
        val (halfX, halfY) = if (width >= height) {
            val hY = worldSquareSize / 2
            val hX = hY * width / height
            hX to hY
        } else {
            val hX = worldSquareSize / 2
            val hY = hX * height / width
            hX to hY
        }

        repeat(height) { yCanvas ->
            val y = halfY - yCanvas * pixelSize
            repeat(width) { xCanvas ->
                val x = -halfX + xCanvas * pixelSize
                val screenPoint = point(x, y, wallZ)
                val vector = screenPoint - light
                val r = ray(light, vector.normalize())
                val xs = s.intersect(r)
                hit(xs)?.run {
                    canvas[xCanvas, yCanvas] = color
                }
            }
        }

        println("ok")
        Window(canvas).show()
    }

}