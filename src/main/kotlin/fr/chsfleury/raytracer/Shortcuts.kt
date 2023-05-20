package fr.chsfleury.raytracer

import fr.chsfleury.raytracer.light.PointLight
import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.linalg.Transform
import fr.chsfleury.raytracer.linalg.Vec4
import fr.chsfleury.raytracer.material.Material
import fr.chsfleury.raytracer.shape.Shape
import fr.chsfleury.raytracer.shape.Sphere
import fr.chsfleury.raytracer.world.World

fun point(x: Double, y: Double, z: Double): Vec4 = Vec4.point(x, y, z)
fun point(x: Number = 0.0, y: Number = 0.0, z: Number = 0.0): Vec4 = point(x.toDouble(), y.toDouble(), z.toDouble())

fun vector(x: Double, y: Double, z: Double): Vec4 = Vec4.vector(x, y, z)
fun vector(x: Number = 0.0, y: Number = 0.0, z: Number = 0.0): Vec4 = vector(x.toDouble(), y.toDouble(), z.toDouble())

fun tuple(x: Double, y: Double, z: Double, w: Double): Vec4 = Vec4.tuple(x, y, z, w)
fun tuple(x: Number, y: Number, z: Number, w: Number): Vec4 = tuple(x.toDouble(), y.toDouble(), z.toDouble(), w.toDouble())

fun ray(origin: Vec4, direction: Vec4): Ray = Ray(origin, direction)

fun sphere(origin: Vec4 = point(), radius: Double = 1.0, material: Material = material(), transform: NDArray = NDArray.ID4): Sphere =
    Sphere(origin, radius, material, transform)

fun intersection(t: Double, obj: Shape): Intersection = Intersection(t, obj)
fun intersection(t: Number, obj: Shape): Intersection = intersection(t.toDouble(), obj)

fun intersections(vararg intersections: Intersection): List<Intersection> = intersections.toList()

fun translation(x: Double, y: Double, z: Double): NDArray = Transform.translation(x, y, z)
fun translation(x: Number, y: Number, z: Number): NDArray = translation(
    x.toDouble(), y.toDouble(), z.toDouble()
)

fun scaling(x: Double, y: Double, z: Double): NDArray = Transform.scaling(x, y, z)
fun scaling(x: Number, y: Number, z: Number): NDArray = scaling(
    x.toDouble(), y.toDouble(), z.toDouble()
)

fun rotationX(rad: Double): NDArray = Transform.xRotation(rad)
fun rotationY(rad: Double): NDArray = Transform.yRotation(rad)
fun rotationZ(rad: Double): NDArray = Transform.zRotation(rad)

fun shearing(xByY: Double, xByZ: Double, yByX: Double, yByZ: Double, zByX: Double, zByY: Double): NDArray =
    Transform.shearing(xByY, xByZ, yByX, yByZ, zByX, zByY)

fun shearing(xByY: Number, xByZ: Number, yByX: Number, yByZ: Number, zByX: Number, zByY: Number): NDArray = shearing(
    xByY.toDouble(), xByZ.toDouble(), yByX.toDouble(), yByZ.toDouble(), zByX.toDouble(), zByY.toDouble()
)

fun color(r: Double, g: Double, b: Double) = Color(r, g, b)
fun color(r: Number, g: Number, b: Number) = color(r.toDouble(), g.toDouble(), b.toDouble())

fun pointLight(position: Vec4, intensity: Color = color(1, 1, 1)): PointLight = PointLight(position, intensity)

fun material(
    color: Color = color(1, 1, 1),
    ambient: Double = 0.1,
    diffuse: Double = 0.9,
    specular: Double = 0.9,
    shininess: Double = 200.0
): Material = Material(color, ambient, diffuse, specular, shininess)

fun world(
    light: PointLight = pointLight(
        point(-10, 10, -10),
        color(1, 1, 1)
    ),
    objects: List<Shape> = emptyList()
): World = World(light, objects)