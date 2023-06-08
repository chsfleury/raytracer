package fr.chsfleury.raytracer.bounds

import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.linalg.Vec4
import fr.chsfleury.raytracer.linalg.Vec4.Companion.point

data class Bounds(
    val minimum: Vec4,
    val maximum: Vec4
) {
    fun corners(transform: NDArray): Sequence<Vec4> = sequenceOf(
        transform * minimum,
        transform * point(minimum.x, minimum.y, maximum.z),
        transform * point(minimum.x, maximum.y, minimum.z),
        transform * point(minimum.x, maximum.y, maximum.z),
        transform * point(maximum.x, minimum.y, minimum.z),
        transform * point(maximum.x, minimum.y, maximum.z),
        transform * point(maximum.x, maximum.y, minimum.z),
        transform * maximum
    )

    companion object {

        fun merge(corners: Sequence<Vec4>): Bounds {
            var minX = Double.POSITIVE_INFINITY
            var minY = Double.POSITIVE_INFINITY
            var minZ = Double.POSITIVE_INFINITY
            var maxX = Double.NEGATIVE_INFINITY
            var maxY = Double.NEGATIVE_INFINITY
            var maxZ = Double.NEGATIVE_INFINITY

            corners.forEach {
                if (it.x < minX) {
                    minX = it.x
                }

                if (it.x > maxX) {
                    maxX = it.x
                }

                if (it.y < minY) {
                    minY = it.y
                }

                if (it.y > maxY) {
                    maxY = it.y
                }

                if (it.z < minZ) {
                    minZ = it.z
                }

                if (it.z > maxZ) {
                    maxZ = it.z
                }
            }

            return Bounds(
                point(minX, minY, minZ),
                point(maxX, maxY, maxZ)
            )
        }

    }
}
