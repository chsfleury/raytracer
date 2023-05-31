package fr.chsfleury.raytracer

import fr.chsfleury.raytracer.Doubles.EPSILON
import fr.chsfleury.raytracer.linalg.Vec4
import fr.chsfleury.raytracer.shape.Shape
import java.lang.Math.pow
import kotlin.math.pow
import kotlin.math.sqrt

data class IntersectionComputation(
    val t: Double,
    val obj: Shape,
    val point: Vec4,
    val eyeVector: Vec4,
    val normalVector: Vec4,
    val inside: Boolean,
    val overPoint: Vec4,
    val reflectVector: Vec4,
    val n1: Double,
    val n2: Double,
    val underPoint: Vec4
) {
    constructor(intersection: Intersection, point: Vec4, eyeVector: Vec4, normalVector: Vec4, inside: Boolean, overPoint: Vec4, reflectVector: Vec4, n1: Double, n2: Double, underPoint: Vec4) : this(
        intersection.t,
        intersection.obj,
        point,
        eyeVector,
        normalVector,
        inside,
        overPoint,
        reflectVector,
        n1,
        n2,
        underPoint
    )

    fun schlick(): Double {
        // find the cosine of the angle between the eye and normal vectors
        var cos = eyeVector dot normalVector

        // total internal reflection can only occur if n1 > n2
        if (n1 > n2) {
            val n = n1 / n2
            val sin2T = n * n * (1.0 - cos * cos)
            if (sin2T > 1.0) {
                return 1.0
            }

            // compute cosine of theta_t using trig identity
            val cosT = sqrt(1.0 - sin2T)

            // when n1 > n2, use cos(theta_t) instead
            cos = cosT
        }

        val r0 = ((n1 - n2) / (n1 + n2)).pow(2)
        return r0 + (1 - r0) * (1 - cos).pow(5)
    }

    companion object {

        fun prepareComputations(hit: Intersection, ray: Ray, intersections: List<Intersection>): IntersectionComputation {
            val point = ray.position(hit.t)
            val normalV = hit.obj.normalAt(point)
            val eyeV = -ray.direction
            val dotNormalEye = normalV dot eyeV
            val inside = dotNormalEye < 0
            val finalNormalV = if (inside) -normalV else normalV
            val overPoint = point + (finalNormalV * EPSILON)
            val underPoint = point - (finalNormalV * EPSILON)
            val reflectVector = ray.direction.reflect(finalNormalV)

            var n1 = 0.0
            var n2 = 0.0
            val containers = mutableListOf<Shape>()

            for (intersection in intersections) {
                if (intersection === hit) {
                    n1 = if (containers.isEmpty()) {
                        1.0
                    } else {
                        containers.last().material.refractiveIndex
                    }
                }

                if (intersection.obj in containers) {
                    containers.remove(intersection.obj)
                } else {
                    containers.add(intersection.obj)
                }

                if (intersection === hit) {
                    n2 = if (containers.isEmpty()) {
                        1.0
                    } else {
                        containers.last().material.refractiveIndex
                    }

                    break
                }
            }

            return IntersectionComputation(
                hit,
                point,
                eyeV,
                finalNormalV,
                inside,
                overPoint,
                reflectVector,
                n1,
                n2,
                underPoint
            )
        }

    }
}
