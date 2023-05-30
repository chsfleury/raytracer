package fr.chsfleury.raytracer.world

import fr.chsfleury.raytracer.Color
import fr.chsfleury.raytracer.Doubles.eq
import fr.chsfleury.raytracer.Intersection
import fr.chsfleury.raytracer.IntersectionComputation
import fr.chsfleury.raytracer.Ray
import fr.chsfleury.raytracer.light.PointLight
import fr.chsfleury.raytracer.linalg.Vec4
import fr.chsfleury.raytracer.prepareComputations
import fr.chsfleury.raytracer.ray
import fr.chsfleury.raytracer.shape.Shape

data class World(
    val light: PointLight,
    val objects: List<Shape>
) {
    fun intersect(ray: Ray): List<Intersection> = objects.asSequence()
        .flatMap { it.intersect(ray) }
        .sortedBy { it.t }
        .toList()

    fun shadeHit(computations: IntersectionComputation, remaining: Int = 5): Color {
        val shadowed = isShadowed(computations.overPoint)
        val surface = computations.obj.material.lighting(
            computations.obj,
            light,
            computations.overPoint,
            computations.eyeVector,
            computations.normalVector,
            shadowed
        )

        val reflected = reflectedColor(computations, remaining)
        return surface + reflected
    }

    fun colorAt(ray: Ray, remaining: Int = 5): Color {
        val xs = intersect(ray)
        return Intersection.hit(xs)?.let {
            val comps = prepareComputations(it, ray, xs)
            shadeHit(comps, remaining)
        } ?: Color.BLACK
    }

    fun isShadowed(point: Vec4): Boolean {
        val v = light.position - point
        val distance = v.magnitude()
        val direction = v.normalize()
        val r = ray(point, direction)
        val xs = intersect(r)
        val h = Intersection.hit(xs)
        return h != null && h.t < distance
    }

    fun reflectedColor(computations: IntersectionComputation, remaining: Int = 5): Color {
        if(remaining < 1 || computations.obj.material.reflective eq 0.0) {
            return Color.BLACK
        }
        val reflectRay = ray(
            computations.overPoint,
            computations.reflectVector
        )
        val color = colorAt(reflectRay, remaining - 1)
        return color * computations.obj.material.reflective
    }
}
