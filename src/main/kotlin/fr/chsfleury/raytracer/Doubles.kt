package fr.chsfleury.raytracer

import kotlin.math.abs
import kotlin.math.roundToInt

object Doubles {

    const val EPSILON = 0.00001
    val NORMALIZED = 0.0..1.0

    infix fun Double.eq(other: Double): Boolean = eq(other, EPSILON)

    fun Double.eq(other: Double, epsilon: Double): Boolean {
        return abs(this - other) <= epsilon
    }

    infix fun Double.neq(other: Double): Boolean = neq(other, EPSILON)

    fun Double.neq(other: Double, epsilon: Double): Boolean = !eq(other, epsilon)

    infix fun Double.eq(other: Number): Boolean = eq(other, EPSILON)

    fun Double.eq(other: Number, epsilon: Double): Boolean {
        return abs(this - other.toDouble()) <= epsilon
    }

    infix fun Double.neq(other: Number): Boolean = neq(other.toDouble())

    fun Double.neq(other: Number, epsilon: Double): Boolean = neq(other.toDouble(), epsilon)

    fun scaleTo255Int(c: Double): Int = (c * 255).coerceIn(0.0, 255.0).roundToInt()

}