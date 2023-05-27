package fr.chsfleury.raytracer.exercises.ballshadow

import fr.chsfleury.raytracer.Color
import fr.chsfleury.raytracer.camera
import fr.chsfleury.raytracer.checkersPattern
import fr.chsfleury.raytracer.color
import fr.chsfleury.raytracer.gradientPattern
import fr.chsfleury.raytracer.solidPattern
import fr.chsfleury.raytracer.material
import fr.chsfleury.raytracer.plane
import fr.chsfleury.raytracer.point
import fr.chsfleury.raytracer.pointLight
import fr.chsfleury.raytracer.radialGradientPattern
import fr.chsfleury.raytracer.ringPattern
import fr.chsfleury.raytracer.rotationX
import fr.chsfleury.raytracer.rotationY
import fr.chsfleury.raytracer.rotationZ
import fr.chsfleury.raytracer.scaling
import fr.chsfleury.raytracer.sphere
import fr.chsfleury.raytracer.stripePattern
import fr.chsfleury.raytracer.swing.Window
import fr.chsfleury.raytracer.translation
import fr.chsfleury.raytracer.vector
import fr.chsfleury.raytracer.viewTransform
import fr.chsfleury.raytracer.world
import kotlin.math.PI

object BallShadow {

    @JvmStatic
    fun main(args: Array<String>) {
        val width = 1200
        val height = 800

        val wallMaterial = material(
            color = color(1, 0.9, 0.9),
            pattern = stripePattern(
                patternA = radialGradientPattern(),
                patternB = stripePattern(
                    patternA = gradientPattern(
                        patternA = solidPattern(color(0, 1, 0)),
                        patternB = solidPattern(Color.BLACK)
                    ),
                    transform = scaling(0.5, 0.5, 0.5)
                )
            ),
            specular = 0.0
        )

        val floor = plane(
            material = wallMaterial
        )

        val middle = sphere(
            transform = rotationY(PI / 4) * translation(-0.5, 1, 0.5),
            material = material(
                pattern = stripePattern(
                    patternA = stripePattern(
                        patternA = solidPattern(color(1, 1, 0)),
                        patternB = solidPattern(Color.WHITE),
                        transform = rotationZ(PI / 3) * scaling(0.3, 0.3, 0.3)
                    ),
                    patternB = solidPattern(color(1, 0, 0)),
                    transform = scaling(0.2, 0.2, 0.2)
                ),
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

        Window(canvas).show()
    }

}