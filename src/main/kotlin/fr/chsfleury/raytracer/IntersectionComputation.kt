package fr.chsfleury.raytracer

import fr.chsfleury.raytracer.linalg.Vec4
import fr.chsfleury.raytracer.shape.Shape

data class IntersectionComputation(
    val t: Double,
    val obj: Shape,
    val point: Vec4,
    val eyeVector: Vec4,
    val normalVector: Vec4
) {
    constructor(intersection: Intersection, point: Vec4, eyeVector: Vec4, normalVector: Vec4) : this(
        intersection.t,
        intersection.obj,
        point,
        eyeVector,
        normalVector
    )

    companion object {

        fun prepareComputations(intersection: Intersection, ray: Ray): IntersectionComputation = ray.position(intersection.t).let { point ->
            IntersectionComputation(
                intersection,
                point,
                -ray.direction,
                intersection.obj.normalAt(point)
            )
        }

    }
}
