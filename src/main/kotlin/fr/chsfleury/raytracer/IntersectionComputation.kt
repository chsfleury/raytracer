package fr.chsfleury.raytracer

import fr.chsfleury.raytracer.linalg.Vec4
import fr.chsfleury.raytracer.shape.Shape

data class IntersectionComputation(
    val t: Double,
    val obj: Shape,
    val point: Vec4,
    val eyeVector: Vec4,
    val normalVector: Vec4,
    val inside: Boolean
) {
    constructor(intersection: Intersection, point: Vec4, eyeVector: Vec4, normalVector: Vec4, inside: Boolean) : this(
        intersection.t,
        intersection.obj,
        point,
        eyeVector,
        normalVector,
        inside
    )

    companion object {

        fun prepareComputations(intersection: Intersection, ray: Ray): IntersectionComputation {
            val point = ray.position(intersection.t)
            val normalV = intersection.obj.normalAt(point)
            val eyeV = -ray.direction
            val dotNormalEye = normalV dot eyeV
            val inside = dotNormalEye < 0
            return IntersectionComputation(
                intersection,
                point,
                eyeV,
                if (inside) -normalV else normalV,
                inside
            )
        }

    }
}
