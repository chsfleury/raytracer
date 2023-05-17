package fr.chsfleury.raytracer

import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.linalg.Transform
import fr.chsfleury.raytracer.linalg.Vec4
import fr.chsfleury.raytracer.shape.Shape
import fr.chsfleury.raytracer.shape.Sphere

fun point(x: Double, y: Double, z: Double): Vec4 = Vec4.point(x, y, z)
fun point(x: Number = 0.0, y: Number = 0.0, z: Number = 0.0): Vec4 = point(x.toDouble(), y.toDouble(), z.toDouble())

fun vector(x: Double, y: Double, z: Double): Vec4 = Vec4.vector(x, y, z)
fun vector(x: Number = 0.0, y: Number = 0.0, z: Number = 0.0): Vec4 = vector(x.toDouble(), y.toDouble(), z.toDouble())

fun tuple(x: Double, y: Double, z: Double, w: Double): Vec4 = Vec4.tuple(x, y, z, w)
fun tuple(x: Number, y: Number, z: Number, w: Number): Vec4 = tuple(x.toDouble(), y.toDouble(), z.toDouble(), w.toDouble())

fun ray(origin: Vec4, direction: Vec4): Ray = Ray(origin, direction)

fun sphere(origin: Vec4 = point(), radius: Double = 1.0, transform: NDArray = NDArray.ID4): Sphere = Sphere(origin, radius, transform)

fun intersection(t: Double, obj: Shape): Intersection = Intersection(t, obj)
fun intersection(t: Number, obj: Shape): Intersection = intersection(t.toDouble(), obj)

fun intersections(vararg intersections: Intersection): List<Intersection> = intersections.toList()

fun translation(x: Double, y: Double, z: Double): NDArray = Transform.translation(x, y, z)
fun translation(x: Number, y: Number, z: Number): NDArray = translation(
    x.toDouble(), y.toDouble(), z.toDouble()
)

fun scaling(x: Double, y: Double, z: Double) = Transform.scaling(x, y, z)
fun scaling(x: Number, y: Number, z: Number) = scaling(
    x.toDouble(), y.toDouble(), z.toDouble()
)