package fr.chsfleury.raytracer.world

import fr.chsfleury.raytracer.Color
import fr.chsfleury.raytracer.Intersection
import fr.chsfleury.raytracer.IntersectionComputation
import fr.chsfleury.raytracer.Ray
import fr.chsfleury.raytracer.light.PointLight
import fr.chsfleury.raytracer.prepareComputations
import fr.chsfleury.raytracer.shape.Shape

data class World(
    val light: PointLight,
    val objects: List<Shape>
) {
    fun intersect(ray: Ray): List<Intersection> = objects.asSequence()
        .flatMap { it.intersect(ray) }
        .sortedBy { it.t }
        .toList()

    fun shadeHit(computations: IntersectionComputation): Color = computations.obj.material.lighting(
        light,
        computations.point,
        computations.eyeVector,
        computations.normalVector
    )

    fun colorAt(ray: Ray): Color {
        val xs = intersect(ray)
        return Intersection.hit(xs)?.let {
            val comps = prepareComputations(it, ray)
            shadeHit(comps)
        } ?: Color.BLACK
    }
}
