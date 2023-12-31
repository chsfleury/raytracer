package fr.chsfleury.raytracer

import fr.chsfleury.raytracer.Color.Companion.WHITE
import fr.chsfleury.raytracer.light.PointLight
import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.linalg.NDArray.Companion.ID4
import fr.chsfleury.raytracer.linalg.Transform
import fr.chsfleury.raytracer.linalg.Vec4
import fr.chsfleury.raytracer.material.Material
import fr.chsfleury.raytracer.noise.Noise
import fr.chsfleury.raytracer.noise.Perlin
import fr.chsfleury.raytracer.pattern.BlendedPattern
import fr.chsfleury.raytracer.pattern.CheckersPattern
import fr.chsfleury.raytracer.pattern.SolidPattern
import fr.chsfleury.raytracer.pattern.SolidPattern.Companion.BLACK_PATTERN
import fr.chsfleury.raytracer.pattern.SolidPattern.Companion.WHITE_PATTERN
import fr.chsfleury.raytracer.pattern.GradientPattern
import fr.chsfleury.raytracer.pattern.Pattern
import fr.chsfleury.raytracer.pattern.PerturbedPattern
import fr.chsfleury.raytracer.pattern.RadialGradientPattern
import fr.chsfleury.raytracer.pattern.RingPattern
import fr.chsfleury.raytracer.pattern.StripePattern
import fr.chsfleury.raytracer.shape.Cone
import fr.chsfleury.raytracer.shape.Cube
import fr.chsfleury.raytracer.shape.Cylinder
import fr.chsfleury.raytracer.shape.Group
import fr.chsfleury.raytracer.shape.Plane
import fr.chsfleury.raytracer.shape.Shape
import fr.chsfleury.raytracer.shape.Sphere
import fr.chsfleury.raytracer.shape.Triangle
import fr.chsfleury.raytracer.world.World
import kotlin.Double.Companion.NEGATIVE_INFINITY
import kotlin.Double.Companion.POSITIVE_INFINITY
import kotlin.math.PI

fun point(x: Double, y: Double, z: Double): Vec4 = Vec4.point(x, y, z)
fun point(x: Number = 0.0, y: Number = 0.0, z: Number = 0.0): Vec4 = point(x.toDouble(), y.toDouble(), z.toDouble())

fun vector(x: Double, y: Double, z: Double): Vec4 = Vec4.vector(x, y, z)
fun vector(x: Number = 0.0, y: Number = 0.0, z: Number = 0.0): Vec4 = vector(x.toDouble(), y.toDouble(), z.toDouble())

fun tuple(x: Double, y: Double, z: Double, w: Double): Vec4 = Vec4.tuple(x, y, z, w)
fun tuple(x: Number, y: Number, z: Number, w: Number): Vec4 = tuple(x.toDouble(), y.toDouble(), z.toDouble(), w.toDouble())

fun ray(origin: Vec4, direction: Vec4): Ray = Ray(origin, direction)

fun sphere(
    material: Material = material(),
    transform: NDArray = ID4,
    parent: Group? = null
): Sphere = Sphere(material, transform, parent)

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

fun color(r: Double, g: Double, b: Double): Color = Color(r, g, b)
fun color(r: Number, g: Number, b: Number): Color = color(r.toDouble(), g.toDouble(), b.toDouble())

fun pointLight(position: Vec4 = point(-10, 10, -10), intensity: Color = color(1, 1, 1)): PointLight = PointLight(position, intensity)

fun material(
    color: Color = color(1, 1, 1),
    pattern: Pattern? = null,
    ambient: Double = 0.1,
    diffuse: Double = 0.9,
    specular: Double = 0.9,
    shininess: Double = 200.0,
    reflective: Double = 0.0,
    transparency: Double = 0.0,
    refractiveIndex: Double = 1.0
): Material = Material(color, pattern, ambient, diffuse, specular, shininess, reflective, transparency, refractiveIndex)

fun world(
    light: PointLight = pointLight(
        point(-10, 10, -10),
        color(1, 1, 1)
    ),
    objects: List<Shape> = emptyList()
): World = World(light, objects)

fun prepareComputations(hit: Intersection, ray: Ray, intersections: List<Intersection> = listOf(hit)) = IntersectionComputation.prepareComputations(hit, ray, intersections)

fun viewTransform(from: Vec4, to: Vec4, up: Vec4): NDArray = Transform.viewTransform(from, to, up)

fun camera(hSize: Int, vSize: Int, fieldOfView: Double = PI / 2, transform: NDArray = ID4) = Camera(hSize, vSize, fieldOfView, transform)

fun plane(
    material: Material = material(),
    transform: NDArray = ID4,
    parent: Group? = null
): Plane = Plane(material, transform, parent)

fun solidPattern(color: Color = WHITE): SolidPattern = SolidPattern(color)

fun stripePattern(patternA: Pattern = WHITE_PATTERN, patternB: Pattern = BLACK_PATTERN, transform: NDArray = ID4): StripePattern = StripePattern(patternA, patternB, transform)

fun gradientPattern(patternA: Pattern = WHITE_PATTERN, patternB: Pattern = BLACK_PATTERN, transform: NDArray = ID4): GradientPattern = GradientPattern(patternA, patternB, transform)
fun ringPattern(patternA: Pattern = WHITE_PATTERN, patternB: Pattern = BLACK_PATTERN, transform: NDArray = ID4): RingPattern = RingPattern(patternA, patternB, transform)

fun checkersPattern(patternA: Pattern = WHITE_PATTERN, patternB: Pattern = BLACK_PATTERN, transform: NDArray = ID4): CheckersPattern = CheckersPattern(patternA, patternB, transform)

fun radialGradientPattern(patternA: Pattern = WHITE_PATTERN, patternB: Pattern = BLACK_PATTERN, transform: NDArray = ID4): RadialGradientPattern = RadialGradientPattern(patternA, patternB, transform)

fun blendPattern(patternA: Pattern = WHITE_PATTERN, patternB: Pattern = BLACK_PATTERN, transform: NDArray = ID4): BlendedPattern = BlendedPattern(patternA, patternB, transform)

fun perturbed(pattern: Pattern, noise: Noise = Perlin, scale: Double = 0.2, transform: NDArray = ID4): PerturbedPattern = PerturbedPattern(pattern, noise, scale, transform)

fun glassSphere(
    material: Material = material(
        transparency = 1.0,
        refractiveIndex = 1.5
    ),
    transform: NDArray = ID4,
    parent: Group? = null
): Sphere = Sphere(
    material,
    transform,
    parent
)

fun cube(
    material: Material = material(),
    transform: NDArray = ID4,
    parent: Group? = null
): Cube = Cube(material, transform, parent)

fun cylinder(
    material: Material = material(),
    transform: NDArray = ID4,
    minimum: Double = NEGATIVE_INFINITY,
    maximum: Double = POSITIVE_INFINITY,
    closed: Boolean = false,
    parent: Group? = null
): Cylinder = Cylinder(material, transform, minimum, maximum, closed, parent)

fun cone(
    material: Material = material(),
    transform: NDArray = ID4,
    minimum: Double = NEGATIVE_INFINITY,
    maximum: Double = POSITIVE_INFINITY,
    closed: Boolean = false,
    parent: Group? = null
): Cone = Cone(material, transform, minimum, maximum, closed, parent)

fun triangle(
    p1: Vec4,
    p2: Vec4,
    p3: Vec4,
    material: Material = material(),
    transform: NDArray = ID4,
    parent: Group? = null
): Triangle = Triangle(p1, p2, p3, material, transform, parent)

fun group(shapes: MutableList<Shape> = mutableListOf(), transform: NDArray = ID4, parent: Group? = null, name: String? = null): Group = Group(shapes, transform, parent, name)