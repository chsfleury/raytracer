package fr.chsfleury.raytracer.shape

import fr.chsfleury.raytracer.Intersection
import fr.chsfleury.raytracer.Ray
import fr.chsfleury.raytracer.linalg.Vec4
import kotlin.math.sqrt

data class Sphere(
    val origin: Vec4,
    val radius: Double
): Shape {
    fun intersect(ray: Ray): Array<Intersection> {
        val sphereToRay = ray.origin - origin
        val a = ray.direction dot ray.direction
        val b = 2 * (ray.direction dot sphereToRay)
        val c = (sphereToRay dot sphereToRay) - 1
        val discriminant = b * b - 4 * a * c

        return if (discriminant < 0) {
            arrayOf()
        } else {
            arrayOf(
                Intersection((-b - sqrt(discriminant)) / (2 * a), this),
                Intersection((-b + sqrt(discriminant)) / (2 * a), this)
            )
        }
    }
}
