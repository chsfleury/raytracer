package fr.chsfleury.raytracer.exercises.cannon

import fr.chsfleury.raytracer.Canvas
import fr.chsfleury.raytracer.Color
import fr.chsfleury.raytracer.Pixel
import fr.chsfleury.raytracer.color
import fr.chsfleury.raytracer.point
import fr.chsfleury.raytracer.swing.Window
import fr.chsfleury.raytracer.vector
import kotlin.math.roundToInt

object Cannon {

    fun tick(environment: Environment, projectile: Projectile): Projectile {
        val position = projectile.position + projectile.velocity
        val velocity = projectile.velocity + environment.gravity + environment.wind
        return Projectile(position, velocity)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val initialProjectile = Projectile(
            point(0, 1, 0),
            vector(1, 1.8, 0).normalize() * 11.25
        )

        val environment = Environment(
            vector(0, -0.1, 0),
            vector(-0.01, 0, 0)
        )

        val height = 550
        val canvas = Canvas(900, height)
        val color = color(1, 1, 1)

        var projectile = initialProjectile

        while (projectile.position.y >= 0.0) {
            projectile = tick(environment, projectile)
            val pixel = Pixel(
                projectile.position.x.roundToInt(),
                height - projectile.position.y.roundToInt()
            )
            if (pixel in canvas) {
                canvas[pixel] = color
            }
        }

//        val fileOutputStream = FileOutputStream("target/output.ppm")
//        canvas.writePPMTo(fileOutputStream)

        Window(canvas).show()
    }

}