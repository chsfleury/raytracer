package fr.chsfleury.raytracer.shape

import fr.chsfleury.raytracer.bounds.Bounds
import fr.chsfleury.raytracer.Intersection
import fr.chsfleury.raytracer.Ray
import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.linalg.Vec4
import fr.chsfleury.raytracer.linalg.Vec4.Companion.ORIGIN
import fr.chsfleury.raytracer.linalg.Vec4.Companion.point
import fr.chsfleury.raytracer.material.Material
import kotlin.math.sqrt

data class Sphere(
    override val material: Material,
    override val transform: NDArray = NDArray.ID4,
    override var parent: Group?
): Shape {
    override val bounds: Bounds = Bounds(
        point(-1.0, -1.0, -1.0),
        point(1.0, 1.0, 1.0)
    )

    override fun localIntersect(localRay: Ray): List<Intersection> {
        val sphereToRay = localRay.origin - ORIGIN
        val a = localRay.direction dot localRay.direction
        val b = 2 * (localRay.direction dot sphereToRay)
        val c = (sphereToRay dot sphereToRay) - 1
        val discriminant = b * b - 4 * a * c

        return if (discriminant < 0) {
            listOf()
        } else {
            listOf(
                Intersection((-b - sqrt(discriminant)) / (2 * a), this),
                Intersection((-b + sqrt(discriminant)) / (2 * a), this)
            )
        }
    }

    override fun localNormalAt(localPoint: Vec4): Vec4 {
        return localPoint - ORIGIN
    }
}
