package fr.chsfleury.raytracer.shape

import fr.chsfleury.raytracer.Doubles.EPSILON
import fr.chsfleury.raytracer.Intersection
import fr.chsfleury.raytracer.Ray
import fr.chsfleury.raytracer.core.PairUtils.asc
import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.linalg.Vec4
import fr.chsfleury.raytracer.material.Material
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

data class Cylinder(
    override val material: Material,
    override val transform: NDArray,
    val minimum: Double,
    val maximum: Double,
    val closed: Boolean
): Shape {
    override fun localIntersect(localRay: Ray): List<Intersection> {
        val a = localRay.direction.x.pow(2) + localRay.direction.z.pow(2)
        val xs = mutableListOf<Intersection>()

        if (a >= EPSILON) {

            val b = 2 * localRay.origin.x * localRay.direction.x + 2 * localRay.origin.z * localRay.direction.z
            val c = localRay.origin.x.pow(2) + localRay.origin.z.pow(2) - 1
            val disc = b * b - 4 * a * c

            if (disc < 0) return listOf()
            val sqrtDisc = sqrt(disc)

            val (t0, t1) = Pair((-b - sqrtDisc) / (2 * a), (-b + sqrtDisc) / (2 * a)).asc()


            val y0 = localRay.origin.y + t0 * localRay.direction.y
            if (minimum < y0 && y0 < maximum) {
                xs += Intersection(t0, this)
            }

            val y1 = localRay.origin.y + t1 * localRay.direction.y
            if (minimum < y1 && y1 < maximum) {
                xs += Intersection(t1, this)
            }
        }

        intersectCaps(localRay, xs)

        return xs
    }

    override fun localNormalAt(localPoint: Vec4): Vec4 {
        val dist = localPoint.x.pow(2) + localPoint.z.pow(2)
        return if (dist < 1 && localPoint.y >= maximum - EPSILON) {
            Vec4.vector(0.0, 1.0, 0.0)
        } else if (dist < 1 && localPoint.y <= minimum + EPSILON) {
            Vec4.vector(0.0, -1.0, 0.0)
        } else {
            Vec4.vector(localPoint.x, 0.0, localPoint.z)
        }
    }

    private fun intersectCaps(ray: Ray, xs: MutableList<Intersection>) {
        // caps only matter if the cylinder is closed, and might possibly be
        // intersected by the ray.
        if (!closed || abs(ray.direction.y) < EPSILON) {
            return
        }

        // check for an intersection with the lower end cap by intersecting
        // the ray with the plane at y = cyl.minimum
        val tMin = (minimum - ray.origin.y) / ray.direction.y
        if (checkCap(ray, tMin)) {
            xs += Intersection(tMin, this)
        }

        // check for an intersection with the upper end cap by intersecting
        // the ray with the plane at y=cyl.maximum
        val tMax = (maximum - ray.origin.y) / ray.direction.y
        if (checkCap(ray, tMax)) {
            xs += Intersection(tMax, this)
        }
    }

    companion object {
        fun checkCap(ray: Ray, t: Double): Boolean {
            val x = ray.origin.x + t * ray.direction.x
            val z = ray.origin.z + t * ray.direction.z
            return x * x + z * z <= 1
        }
    }
}