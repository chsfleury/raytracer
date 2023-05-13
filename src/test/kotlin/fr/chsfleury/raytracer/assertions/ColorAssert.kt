package fr.chsfleury.raytracer.assertions

import fr.chsfleury.raytracer.Color
import org.assertj.core.api.AbstractAssert

class ColorAssert(actual: Color): AbstractAssert<ColorAssert, Color>(actual, ColorAssert::class.java) {
    companion object {
        fun assertThatColor(actual: Color) = ColorAssert(actual)
    }

    fun isEqualTo(other: Color) = also {
        isNotNull
        val equality = actual eq other
        if (!equality) {
            failWithMessage("Actual color does not equal to expected one")
        }
    }
}