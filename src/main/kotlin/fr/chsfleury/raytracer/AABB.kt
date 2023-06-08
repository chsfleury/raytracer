package fr.chsfleury.raytracer

import fr.chsfleury.raytracer.bounds.Bounds

object AABB {

    fun intersect(bounds: Bounds, ray: Ray): List<Double> {
        var (tMin, tMax) = checkAxis(bounds.minimum.x, bounds.maximum.x, ray.origin.x, ray.direction.x)
        val (tyMin, tyMax) = checkAxis(bounds.minimum.y, bounds.maximum.y, ray.origin.y, ray.direction.y)

        if ((tMin > tyMax) || (tyMin > tMax)) {
            return listOf()
        }

        if (tyMin > tMin) {
            tMin = tyMin
        }

        if (tyMax < tMax) {
            tMax = tyMax
        }

        val (tzMin, tzMax) = checkAxis(bounds.minimum.z, bounds.maximum.z, ray.origin.z, ray.direction.z)

        if ((tMin > tzMax) || (tzMin > tMax)) {
            return listOf()
        }

        if (tzMin > tMin) {
            tMin = tzMin
        }

        if (tzMax < tMax) {
            tMax = tzMax
        }

        return listOf(tMin, tMax)
    }

    private fun checkAxis(minimum: Double, maximum: Double, origin: Double, direction: Double): Pair<Double, Double> {
        val tMin = (minimum - origin) / direction
        val tMax = (maximum - origin) / direction

        return if (tMin > tMax) {
            tMax to tMin
        } else {
            tMin to tMax
        }
    }

}