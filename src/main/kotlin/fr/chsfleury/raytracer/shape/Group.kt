package fr.chsfleury.raytracer.shape

import fr.chsfleury.raytracer.AABB
import fr.chsfleury.raytracer.Intersection
import fr.chsfleury.raytracer.Ray
import fr.chsfleury.raytracer.bounds.Bounds
import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.linalg.Vec4
import fr.chsfleury.raytracer.material

data class Group(
    private val shapes: MutableList<Shape>,
    override val transform: NDArray,
    override var parent: Group?,
    val name: String? = null
): Shape {
    override val material = material()
    override val bounds: Bounds = computeBounds()

    fun add(shape: Shape) = shape
        .also { it.parent = parent }
        .run { shapes.add(this) }

    fun addAll(newShapes: Collection<Shape>) {
        shapes.addAll(newShapes)
        newShapes.forEach { it.parent = parent }
    }

    operator fun get(i: Int): Shape = shapes[i]

    fun size() = shapes.size

    init {
        shapes.forEach { it.parent = this }
    }

    override fun localIntersect(localRay: Ray): List<Intersection> {
        if (AABB.intersect(bounds, localRay).isEmpty()) {
            return emptyList()
        }
        return shapes.asSequence()
            .flatMap { it.intersect(localRay).asSequence() }
            .sortedBy { it.t }
            .toList()
    }

    override fun localNormalAt(localPoint: Vec4): Vec4 {
        error("unsupported")
    }

    fun isEmpty() = shapes.isEmpty()

    private fun computeBounds(): Bounds = Bounds.merge(
        shapes.asSequence().flatMap { it.bounds.corners(it.transform) }
    )

}
