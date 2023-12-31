package fr.chsfleury.raytracer.assertions

import fr.chsfleury.raytracer.Doubles.EPSILON
import fr.chsfleury.raytracer.Doubles.eq
import fr.chsfleury.raytracer.linalg.Vec4
import org.assertj.core.api.AbstractAssert

class Vec4Assert(actual: Vec4): AbstractAssert<Vec4Assert, Vec4>(actual, Vec4Assert::class.java) {
    companion object {
        fun assertThatVec4(actual: Vec4) = Vec4Assert(actual)
    }

    fun isAPoint() = also {
        if (!actual.isPoint()) {
            failWithMessage("Actual tuple is not a point")
        }
    }

    fun isNotAPoint() = also {
        if (actual.isPoint()) {
            failWithMessage("Actual tuple is a point")
        }
    }

    fun isAVector() = also {
        if (!actual.isVector()) {
            failWithMessage("Actual tuple is not a vector")
        }
    }

    fun isNotAVector() = also {
        if (actual.isVector()) {
            failWithMessage("Actual tuple is a vector")
        }
    }

    fun isEqualTo(other: Vec4, epsilon: Double = EPSILON) = also {
        val equality = actual.eq(other, epsilon)
        if (!equality) {
            failWithMessage("Actual tuple does not equal to expected one. current: $actual, expected: $other")
        }
    }

    fun hasMagnitude(expected: Double) = also {
        val magnitude = actual.magnitude()
        val equality = magnitude eq expected
        if (!equality) {
            failWithMessage("actual tuple magnitude is different than %f", expected)
        }
    }

    fun hasMagnitude(expected: Number) = hasMagnitude(expected.toDouble())
}