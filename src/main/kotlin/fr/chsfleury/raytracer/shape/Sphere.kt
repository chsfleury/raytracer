package fr.chsfleury.raytracer.shape

import fr.chsfleury.raytracer.Intersection
import fr.chsfleury.raytracer.Ray
import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.linalg.Vec4
import kotlin.math.sqrt

data class Sphere(
    val origin: Vec4,
    val radius: Double,
    var transform: NDArray = NDArray.ID4
): Shape {
    fun intersect(ray: Ray): List<Intersection> {
        val transformedRay = transform.inverse() * ray
        val sphereToRay = transformedRay.origin - origin
        val a = transformedRay.direction dot transformedRay.direction
        val b = 2 * (transformedRay.direction dot sphereToRay)
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
}
