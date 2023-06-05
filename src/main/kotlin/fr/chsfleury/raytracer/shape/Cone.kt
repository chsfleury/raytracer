package fr.chsfleury.raytracer.shape

import fr.chsfleury.raytracer.Doubles.isNearZero
import fr.chsfleury.raytracer.Doubles.isNotZero
import fr.chsfleury.raytracer.Intersection
import fr.chsfleury.raytracer.Ray
import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.linalg.Vec4
import fr.chsfleury.raytracer.material.Material
import kotlin.math.pow
import kotlin.math.sqrt

data class Cone(
    override val material: Material,
    override val transform: NDArray,
    override val minimum: Double,
    override val maximum: Double,
    override val closed: Boolean
): CylinderBased {

    override fun a(localRay: Ray): Double = localRay.direction.x.pow(2) - localRay.direction.y.pow(2) + localRay.direction.z.pow(2)

    override fun b(localRay: Ray): Double = 2 * localRay.origin.x * localRay.direction.x - 2 * localRay.origin.y * localRay.direction.y + 2 * localRay.origin.z * localRay.direction.z

    override fun c(localRay: Ray): Double = localRay.origin.x.pow(2) - localRay.origin.y.pow(2) + localRay.origin.z.pow(2)
    override fun capsRadius(y: Double): Double = y

    override fun intersectWalls(ray: Ray, xs: MutableList<Intersection>) {
        val a = a(ray)
        if (a.isNearZero()) {
            val b = b(ray)
            if (b.isNotZero()) {
                val c = c(ray)
                xs += Intersection(-c / (2 * b), this)
            }
        } else {
            findWallIntersections(a, ray, xs)
        }
    }

    override fun localNormalAt(localPoint: Vec4): Vec4 {
        val y = sqrt(localPoint.x.pow(2) + localPoint.z.pow(2)).let { y ->
            if (localPoint.y > 0) {
                -y
            } else {
                y
            }
        }
        return Vec4.vector(localPoint.x, y, localPoint.z)
    }
}
