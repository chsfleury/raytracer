package fr.chsfleury.raytracer

import fr.chsfleury.raytracer.shape.Shape

data class Intersection(
    val t: Double,
    val obj: Shape
) {
    companion object {
        fun hit(intersections: List<Intersection>): Intersection? {
            return intersections.asSequence()
                .filter { it.t > 0 }
                .minByOrNull(Intersection::t)
        }
    }
}
