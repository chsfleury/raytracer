package fr.chsfleury.raytracer

import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.world.World
import kotlin.math.tan

data class Camera(
    val hSize: Int,
    val vSize: Int,
    val fieldOfView: Double,
    val transform: NDArray
) {
    val pixelSize: Double
    val halfWidth: Double
    val halfHeight: Double

    init {
        val halfView = tan(fieldOfView / 2)
        val aspect = 1.0 * hSize / vSize

        if (aspect >= 1) {
            halfWidth = halfView
            halfHeight = halfView / aspect
        } else {
            halfWidth = halfView * aspect
            halfHeight = halfView
        }
        pixelSize = (halfWidth * 2) / hSize
    }

    fun rayForPixel(px: Int, py: Int): Ray {
        val xOffset = (px + 0.5) * pixelSize
        val yOffset = (py + 0.5) * pixelSize
        val worldX = halfWidth - xOffset
        val worldY = halfHeight - yOffset
        val transformInverse = transform.inverse()
        val pixel = transformInverse * point(worldX, worldY, -1)
        val origin = transformInverse * point(0, 0, 0)
        val direction = (pixel - origin).normalize()
        return Ray(origin, direction)
    }

    fun render(world: World): Canvas {
        val canvas = Canvas(hSize, vSize)

        repeat(vSize) { y ->
            repeat(hSize) { x ->
                val ray = rayForPixel(x, y)
                val color = world.colorAt(ray)
                canvas[x, y] = color
            }
        }

        return canvas
    }
}
