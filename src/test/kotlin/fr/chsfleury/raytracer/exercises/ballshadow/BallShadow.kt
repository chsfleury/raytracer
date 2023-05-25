package fr.chsfleury.raytracer.exercises.ballshadow

import fr.chsfleury.raytracer.Canvas
import fr.chsfleury.raytracer.Color
import fr.chsfleury.raytracer.Intersection.Companion.hit
import fr.chsfleury.raytracer.camera
import fr.chsfleury.raytracer.color
import fr.chsfleury.raytracer.material
import fr.chsfleury.raytracer.plane
import fr.chsfleury.raytracer.point
import fr.chsfleury.raytracer.pointLight
import fr.chsfleury.raytracer.ray
import fr.chsfleury.raytracer.rotationX
import fr.chsfleury.raytracer.rotationY
import fr.chsfleury.raytracer.rotationZ
import fr.chsfleury.raytracer.scaling
import fr.chsfleury.raytracer.shape.Sphere
import fr.chsfleury.raytracer.shearing
import fr.chsfleury.raytracer.sphere
import fr.chsfleury.raytracer.swing.Window
import fr.chsfleury.raytracer.translation
import fr.chsfleury.raytracer.vector
import fr.chsfleury.raytracer.viewTransform
import fr.chsfleury.raytracer.world
import kotlin.math.PI
import kotlin.math.min

object BallShadow {

    @JvmStatic
    fun main(args: Array<String>) {
        val width = 600
        val height = 400

        val wallMaterial = material(
            color = color(1, 0.9, 0.9),
            specular = 0.0
        )

//        val floor = sphere(
//            transform = scaling(10, 0.01, 10),
//            material = wallMaterial
//        )
//
//        val leftWall = sphere(
//            transform = translation(0, 0, 5) * rotationY(-PI / 4) * rotationX(PI / 2) * scaling(10, 0.01, 10),
//            material = wallMaterial
//        )
//
//        val rightWall = sphere(
//            transform = translation(0, 0, 5) * rotationY(PI / 4) * rotationX(PI / 2) * scaling(10, 0.01, 10),
//            material = wallMaterial
//        )

        val floor = plane(material = wallMaterial)

        val middle = sphere(
            transform = translation(-0.5, 1, 0.5),
            material = material(
                color = color(0.1, 1, 0.5),
                diffuse = 0.7,
                specular = 0.3
            )
        )

        val right = sphere(
            transform = translation(1.5, 0.5, -0.5) * scaling(0.5, 0.5, 0.5),
            material = material(
                color = color(0.5, 1, 0.1),
                diffuse = 0.7,
                specular = 0.3
            )
        )

        val left = sphere(
            transform = translation(-1.5, 0.33, -0.75) * scaling(0.33, 0.33, 0.33),
            material = material(
                color = color(1, 0.8, 0.1),
                diffuse = 0.7,
                specular = 0.3
            )
        )

        val world = world(
            pointLight(
                point(-10, 10, -10),
                color(1, 1, 1)
            ),
            //listOf(floor, leftWall, rightWall, middle, right, left)
            listOf(floor, middle, right, left)
        )

        val camera = camera(
            width,
            height,
            PI / 3,
            viewTransform(
                point(0, 1.5, -5),
                point(0, 1, 0),
                vector(0, 1, 0)
            )
        )

        val canvas = camera.render(world)

        println("ok")
        Window(canvas).show()
    }

}