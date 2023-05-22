package fr.chsfleury.raytracer.linalg

import fr.chsfleury.raytracer.linalg.NDArray.Companion.ID4
import kotlin.math.cos
import kotlin.math.sin

object Transform {

    fun translation(x: Double, y: Double, z: Double): NDArray = NDArray.of4x4(
        1, 0, 0, x,
        0, 1, 0, y,
        0, 0, 1, z,
        0, 0, 0, 1
    )

    fun translation(x: Number, y: Number, z: Number): NDArray = translation(x.toDouble(), y.toDouble(), z.toDouble())

    fun scaling(x: Double, y: Double, z: Double): NDArray = NDArray.of4x4(
        x, 0, 0, 0,
        0, y, 0, 0,
        0, 0, z, 0,
        0, 0, 0, 1
    )

    fun scaling(xScale: Number, yScale: Number, zScale: Number) = Transform.scaling(xScale.toDouble(), yScale.toDouble(), zScale.toDouble())

    fun xRotation(r: Double): NDArray = NDArray.of4x4(
        1, 0, 0, 0,
        0, cos(r), -sin(r), 0,
        0, sin(r), cos(r), 0,
        0, 0, 0, 1
    )

    fun yRotation(r: Double): NDArray = NDArray.of4x4(
        cos(r), 0, sin(r), 0,
        0, 1, 0, 0,
        -sin(r), 0, cos(r), 0,
        0, 0, 0, 1
    )

    fun zRotation(r: Double): NDArray = NDArray.of4x4(
        cos(r), -sin(r), 0, 0,
        sin(r), cos(r), 0, 0,
        0, 0, 1, 0,
        0, 0, 0, 1
    )

    fun shearing(xByY: Double, xByZ: Double, yByX: Double, yByZ: Double, zByX: Double, zByY: Double): NDArray = NDArray.of4x4(
        1, xByY, xByZ, 0,
        yByX, 1, yByZ, 0,
        zByX, zByY, 1, 0,
        0, 0, 0, 1
    )

    fun shearing(xByY: Number, xByZ: Number, yByX: Number, yByZ: Number, zByX: Number, zByY: Number) = shearing(
        xByY.toDouble(), xByZ.toDouble(), yByX.toDouble(), yByZ.toDouble(), zByX.toDouble(), zByY.toDouble()
    )

    fun viewTransform(from: Vec4, to: Vec4, up: Vec4): NDArray {
        if (!from.isPoint()) {
            error("from must be a point")
        }

        if (!to.isPoint()) {
            error("to must be a point")
        }

        if (!up.isVector()) {
            error("up must be a vector")
        }

        val forward = (to - from).normalize()
        val normalizedUp = up.normalize()
        val left = forward cross normalizedUp
        val trueUp = left cross forward

        val orientation = NDArray.of4x4(
            left.x, left.y, left.z, 0,
            trueUp.x, trueUp.y, trueUp.z, 0,
            -forward.x, -forward.y, -forward.z, 0,
            0, 0, 0, 1
        )
        return orientation * translation(-from.x, -from.y, -from.z)
    }
}