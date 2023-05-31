package fr.chsfleury.raytracer.assertions

import fr.chsfleury.raytracer.Color
import fr.chsfleury.raytracer.Doubles.EPSILON
import org.assertj.core.api.AbstractAssert

class ColorAssert(actual: Color): AbstractAssert<ColorAssert, Color>(actual, ColorAssert::class.java) {
    companion object {
        fun assertThatColor(actual: Color) = ColorAssert(actual)
    }

    fun isEqualTo(other: Color, epsilon: Double = EPSILON) = also {
        val equality = actual.eq(other, epsilon)
        if (!equality) {
            failWithMessage("Actual color does not equal to expected one, current: $actual, expected: $other")
        }
    }
}