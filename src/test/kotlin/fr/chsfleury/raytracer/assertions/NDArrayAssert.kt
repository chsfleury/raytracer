package fr.chsfleury.raytracer.assertions

import fr.chsfleury.raytracer.linalg.NDArray
import org.assertj.core.api.AbstractAssert

class NDArrayAssert(actual: NDArray): AbstractAssert<NDArrayAssert, NDArray>(actual, NDArrayAssert::class.java) {
    companion object {
        fun assertThatNDArray(actual: NDArray) = NDArrayAssert(actual)
    }

    fun isEqualTo(other: NDArray) = also {
        val equality = actual eq other
        if (!equality) {
            failWithMessage("Actual NDArray does not equal to expected one")
        }
    }

    fun isNotEqualTo(other: NDArray) = also {
        val equality = actual eq other
        if (equality) {
            failWithMessage("Actual NDArray equals to expected one")
        }
    }
}