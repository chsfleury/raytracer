package fr.chsfleury.raytracer.shape

import fr.chsfleury.raytracer.bounds.Bounds
import fr.chsfleury.raytracer.Doubles.EPSILON
import fr.chsfleury.raytracer.Doubles.isNotZero
import fr.chsfleury.raytracer.Intersection
import fr.chsfleury.raytracer.Ray
import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.linalg.Vec4
import fr.chsfleury.raytracer.linalg.Vec4.Companion.point
import fr.chsfleury.raytracer.material.Material
import kotlin.math.pow

data class Cylinder(
    override val material: Material,
    override val transform: NDArray,
    override val minimum: Double,
    override val maximum: Double,
    override val closed: Boolean,
    override var parent: Group?
): CylinderBased {

    override fun intersectWalls(ray: Ray, xs: MutableList<Intersection>) {
        val a = a(ray)
        if (a.isNotZero()) {
            findWallIntersections(a, ray, xs)
        }
    }

    override val bounds: Bounds = Bounds(
        point(-1.0, minimum, -1.0),
        point(1.0, maximum, 1.0)
    )

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

    override fun a(localRay: Ray): Double = localRay.direction.x.pow(2) + localRay.direction.z.pow(2)

    override fun b(localRay: Ray): Double = 2 * localRay.origin.x * localRay.direction.x + 2 * localRay.origin.z * localRay.direction.z

    override fun c(localRay: Ray): Double = localRay.origin.x.pow(2) + localRay.origin.z.pow(2) - 1
    override fun capsRadius(y: Double): Double = 1.0
}