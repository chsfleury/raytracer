package fr.chsfleury.raytracer

import kotlin.math.abs
import kotlin.math.roundToInt

object Doubles {

    const val EPSILON = 0.00001
    val NORMALIZED = 0.0..1.0

    infix fun Double.eq(other: Double): Boolean {
        return abs(this - other) < EPSILON
    }

    infix fun Double.neq(other: Double): Boolean = !eq(other)

    infix fun Double.eq(other: Number): Boolean {
        return abs(this - other.toDouble()) < EPSILON
    }

    infix fun Double.neq(other: Number): Boolean = neq(other.toDouble())

    fun scaleTo255Int(c: Double): Int = (c * 255).coerceIn(0.0, 255.0).roundToInt()

}