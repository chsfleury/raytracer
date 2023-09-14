package fr.chsfleury.raytracer.exercises.wavefront

import fr.chsfleury.raytracer.camera
import fr.chsfleury.raytracer.io.wavefront.WavefrontReader
import fr.chsfleury.raytracer.point
import fr.chsfleury.raytracer.pointLight
import fr.chsfleury.raytracer.swing.Window
import fr.chsfleury.raytracer.vector
import fr.chsfleury.raytracer.viewTransform
import java.nio.file.Paths
import kotlin.math.PI

object Wavefront {

    @JvmStatic
    fun main(args: Array<String>) {
        val width = 400
        val height = 400

        val camera = camera(
            width,
            height,
            PI / 3,
            viewTransform(
                point(-180, -100, 130),
                point(-200, 0, 50),
                vector(0, 0, 1)
            )
        )

        val wavefrontFile = Paths.get("./models/obj/teapot_med.obj")
        val pointLight = pointLight(point(-150, 0, 1000))
        val scene = WavefrontReader.read(wavefrontFile, pointLight)
        val canvas = camera.render(scene.world)

        Window(canvas).show()
    }


}