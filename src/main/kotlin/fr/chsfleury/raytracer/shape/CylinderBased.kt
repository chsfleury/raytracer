package fr.chsfleury.raytracer.shape

import fr.chsfleury.raytracer.Doubles
import fr.chsfleury.raytracer.Intersection
import fr.chsfleury.raytracer.Ray
import fr.chsfleury.raytracer.core.PairUtils.asc
import kotlin.math.abs
import kotlin.math.sqrt

interface CylinderBased: Shape {
    val minimum: Double
    val maximum: Double
    val closed: Boolean

    fun a(localRay: Ray): Double
    fun b(localRay: Ray): Double
    fun c(localRay: Ray): Double

    fun capsRadius(y:  Double): Double

    override fun localIntersect(localRay: Ray): List<Intersection> {
        val xs = mutableListOf<Intersection>()
        intersectWalls(localRay, xs)
        intersectCaps(localRay, xs)
        return xs
    }

    fun intersectWalls(ray: Ray, xs: MutableList<Intersection>)

    fun findWallIntersections(a: Double, localRay: Ray, xs: MutableList<Intersection>) {
        val b = b(localRay)
        val c = c(localRay)
        val disc = b * b - 4 * a * c

        if (disc >= 0) {
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
    }

    fun intersectCaps(ray: Ray, xs: MutableList<Intersection>) {
        // caps only matter if the cylinder is closed, and might be
        // intersected by the ray.
        if (!closed || abs(ray.direction.y) < Doubles.EPSILON) {
            return
        }

        // check for an intersection with the lower end cap by intersecting
        // the ray with the plane at y = cyl.minimum
        val tMin = (minimum - ray.origin.y) / ray.direction.y
        if (checkCap(ray, tMin, capsRadius(minimum))) {
            xs += Intersection(tMin, this)
        }

        // check for an intersection with the upper end cap by intersecting
        // the ray with the plane at y=cyl.maximum
        val tMax = (maximum - ray.origin.y) / ray.direction.y
        if (checkCap(ray, tMax, capsRadius(maximum))) {
            xs += Intersection(tMax, this)
        }
    }

    private fun checkCap(ray: Ray, t: Double, y: Double): Boolean {
        val x = ray.origin.x + t * ray.direction.x
        val z = ray.origin.z + t * ray.direction.z
        return x * x + z * z <= y * y
    }
}