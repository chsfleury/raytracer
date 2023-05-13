package fr.chsfleury.raytracer.assertions

import fr.chsfleury.raytracer.Doubles.eq
import fr.chsfleury.raytracer.Tuple
import org.assertj.core.api.AbstractAssert

class TupleAssert(actual: Tuple): AbstractAssert<TupleAssert, Tuple>(actual, TupleAssert::class.java) {
    companion object {
        fun assertThatTuple(actual: Tuple) = TupleAssert(actual)
    }

    fun isAPoint() = also {
        isNotNull
        if (!actual.isPoint()) {
            failWithMessage("Actual tuple is not a point")
        }
    }

    fun isNotAPoint() = also {
        isNotNull
        if (actual.isPoint()) {
            failWithMessage("Actual tuple is a point")
        }
    }

    fun isAVector() = also {
        isNotNull
        if (!actual.isVector()) {
            failWithMessage("Actual tuple is not a vector")
        }
    }

    fun isNotAVector() = also {
        isNotNull
        if (actual.isVector()) {
            failWithMessage("Actual tuple is a vector")
        }
    }

    fun isEqualTo(other: Tuple) = also {
        isNotNull
        val equality = actual eq other
        if (!equality) {
            failWithMessage("Actual tuple does not equal to expected one")
        }
    }

    fun hasMagnitude(expected: Double) = also {
        isNotNull
        val magnitude = actual.magnitude()
        val equality = magnitude eq expected
        if (!equality) {
            failWithMessage("actual tuple magnitude is different than %f", expected)
        }
    }

    fun hasMagnitude(expected: Number) = hasMagnitude(expected.toDouble())
}