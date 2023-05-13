package fr.chsfleury.raytracer.assertions

import fr.chsfleury.raytracer.Doubles.eq
import org.assertj.core.api.AbstractAssert

class DoubleAssert(actual: Double): AbstractAssert<DoubleAssert, Double>(actual, DoubleAssert::class.java) {
    companion object {
        fun assertThatDouble(actual: Double) = DoubleAssert(actual)
    }

    fun isEqualTo(other: Double) = also {
        val equality = actual eq other
        if (!equality) {
            failWithMessage("Actual Double does not equal to expected one")
        }
    }

    fun isEqualTo(other: Number) = isEqualTo(other.toDouble())

    fun isNotEqualTo(other: Double) = also {
        val equality = actual eq other
        if (equality) {
            failWithMessage("Actual Double equals to expected one")
        }
    }

    fun isNotEqualTo(other: Number) = isEqualTo(other.toDouble())
}