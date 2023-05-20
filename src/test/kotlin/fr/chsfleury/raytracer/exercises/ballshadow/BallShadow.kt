package fr.chsfleury.raytracer.exercises.ballshadow

import fr.chsfleury.raytracer.Canvas
import fr.chsfleury.raytracer.Color
import fr.chsfleury.raytracer.Intersection.Companion.hit
import fr.chsfleury.raytracer.color
import fr.chsfleury.raytracer.material
import fr.chsfleury.raytracer.point
import fr.chsfleury.raytracer.pointLight
import fr.chsfleury.raytracer.ray
import fr.chsfleury.raytracer.rotationY
import fr.chsfleury.raytracer.rotationZ
import fr.chsfleury.raytracer.scaling
import fr.chsfleury.raytracer.shape.Sphere
import fr.chsfleury.raytracer.shearing
import fr.chsfleury.raytracer.sphere
import fr.chsfleury.raytracer.swing.Window
import kotlin.math.PI
import kotlin.math.min

object BallShadow {

    @JvmStatic
    fun main(args: Array<String>) {
        val width = 600
        val height = 400

        val canvas = Canvas(width, height)

        val material = material(color = color(1, 0.2, 1))
        val s = sphere(material = material)

        val eye = point(0, 0, -5)
        val light = pointLight(
            point(-10, 10, -10),
            color(1, 1, 1)
        )
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
                val vector = screenPoint - eye
                val r = ray(eye, vector.normalize())
                val xs = s.intersect(r)
                hit(xs)?.run {
                    val point = r.position(t)
                    val normal = obj.normalAt(point)
                    val eyev = -r.direction
                    canvas[xCanvas, yCanvas] = obj.material.lighting(light, point, eyev, normal)
                }
                if (xCanvas % 100 == 0) {
                    println("($xCanvas, $yCanvas)")
                }
            }
        }

        println("ok")
        Window(canvas).show()
    }

}