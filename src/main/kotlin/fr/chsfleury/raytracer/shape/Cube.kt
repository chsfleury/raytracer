package fr.chsfleury.raytracer.shape

import fr.chsfleury.raytracer.Intersection
import fr.chsfleury.raytracer.Ray
import fr.chsfleury.raytracer.core.PairUtils.asc
import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.linalg.Vec4
import fr.chsfleury.raytracer.material.Material
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class Cube(
    override val material: Material,
    override val transform: NDArray
) : Shape {

    override fun localIntersect(localRay: Ray): List<Intersection> {
        var (tMin, tMax) = checkAxis(localRay.origin.x, localRay.direction.x)
        val (tyMin, tyMax) = checkAxis(localRay.origin.y, localRay.direction.y)

        if ((tMin > tyMax) || (tyMin > tMax)) {
            return listOf()
        }

        if (tyMin > tMin) {
            tMin = tyMin
        }

        if (tyMax < tMax) {
            tMax = tyMax
        }

        val (tzMin, tzMax) = checkAxis(localRay.origin.z, localRay.direction.z)

        if ((tMin > tzMax) || (tzMin > tMax)) {
            return listOf()
        }

        if (tzMin > tMin) {
            tMin = tzMin
        }

        if (tzMax < tMax) {
            tMax = tzMax
        }

        return listOf(
            Intersection(tMin, this),
            Intersection(tMax, this)
        )
    }

    override fun localNormalAt(localPoint: Vec4): Vec4 {
        val absX = abs(localPoint.x)
        val absY = abs(localPoint.y)
        val absZ = abs(localPoint.z)
        if (absX >= absY) {
            if (absX >= absZ) {
                return Vec4.vector(localPoint.x, 0.0, 0.0)
            }
        } else if (absY >= absZ) {
            return Vec4.vector(0.0, localPoint.y, 0.0)
        }
        return Vec4.vector(0.0, 0.0, localPoint.z)
    }

    private fun checkAxis(origin: Double, direction: Double): Pair<Double, Double> {
        val tMin = (-1 - origin) / direction
        val tMax = (1 - origin) / direction

        return if (tMin > tMax) {
            tMax to tMin
        } else {
            tMin to tMax
        }
    }

}
