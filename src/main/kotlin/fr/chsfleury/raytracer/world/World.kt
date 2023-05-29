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

    fun shadeHit(computations: IntersectionComputation): Color {
        val shadowed = isShadowed(computations.overPoint)
        val surface = computations.obj.material.lighting(
            computations.obj,
            light,
            computations.overPoint,
            computations.eyeVector,
            computations.normalVector,
            shadowed
        )

        val reflected = reflectedColor(computations)
        return surface + reflected
    }

    fun colorAt(ray: Ray): Color {
        val xs = intersect(ray)
        return Intersection.hit(xs)?.let {
            val comps = prepareComputations(it, ray)
            shadeHit(comps)
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

    fun reflectedColor(computations: IntersectionComputation): Color {
        if(computations.obj.material.reflective eq 0.0) {
            return Color.BLACK
        }
        val reflectRay = ray(
            computations.overPoint,
            computations.reflectVector
        )
        val color = colorAt(reflectRay)
        return color * computations.obj.material.reflective
    }
}
