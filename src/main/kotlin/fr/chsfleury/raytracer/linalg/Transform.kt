package fr.chsfleury.raytracer.linalg

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
}