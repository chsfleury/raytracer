package fr.chsfleury.raytracer.shape

import fr.chsfleury.raytracer.Doubles.EPSILON
import fr.chsfleury.raytracer.Intersection
import fr.chsfleury.raytracer.Ray
import fr.chsfleury.raytracer.bounds.Bounds
import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.linalg.Vec4
import fr.chsfleury.raytracer.material.Material
import kotlin.math.abs

data class Triangle(
    val p1: Vec4,
    val p2: Vec4,
    val p3: Vec4,
    override val material: Material,
    override val transform: NDArray,
    override var parent: Group?
): Shape {
    val e1 = p2 - p1
    val e2 = p3 - p1
    val normal = (e2 cross e1).normalize()

    override val bounds: Bounds = Bounds.merge(sequenceOf(p1, p2, p3))

    override fun localIntersect(localRay: Ray): List<Intersection> {
        val crossE2 = localRay.direction cross e2
        val det = e1 dot crossE2
        if (abs(det) < EPSILON) {
            return listOf()
        }

        val f = 1.0 / det
        val p1ToOrigin = localRay.origin - p1
        val u = f * (p1ToOrigin dot crossE2)
        if (u < 0 || u > 1) {
            return listOf()
        }

        val originCrossE1 = p1ToOrigin cross e1
        val v = f * (localRay.direction dot originCrossE1)
        if (v < 0 || (u + v) > 1) {
            return listOf()
        }

        val t = f * (e2 dot originCrossE1)
        return listOf(Intersection(t, this))
    }

    override fun localNormalAt(localPoint: Vec4): Vec4 = normal
}
