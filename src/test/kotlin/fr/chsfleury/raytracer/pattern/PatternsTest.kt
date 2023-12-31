package fr.chsfleury.raytracer.pattern

import fr.chsfleury.raytracer.Color
import fr.chsfleury.raytracer.assertions.ColorAssert.Companion.assertThatColor
import fr.chsfleury.raytracer.assertions.NDArrayAssert.Companion.assertThatNDArray
import fr.chsfleury.raytracer.checkersPattern
import fr.chsfleury.raytracer.color
import fr.chsfleury.raytracer.gradientPattern
import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.linalg.NDArray.Companion.ID4
import fr.chsfleury.raytracer.point
import fr.chsfleury.raytracer.ringPattern
import fr.chsfleury.raytracer.scaling
import fr.chsfleury.raytracer.sphere
import fr.chsfleury.raytracer.stripePattern
import fr.chsfleury.raytracer.translation
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PatternsTest {

    @Test
    fun `Creating a stripe pattern` () {
        val pattern = stripePattern()
        assertThat(pattern.patternA).isEqualTo(SolidPattern.WHITE_PATTERN)
        assertThat(pattern.patternB).isEqualTo(SolidPattern.BLACK_PATTERN)
    }

    @Test
    fun `A stripe pattern is constant in y` () {
        val pattern = stripePattern()
        assertThatColor(pattern.colorAt(point(0, 0, 0)))
            .isEqualTo(Color.WHITE)
        assertThatColor(pattern.colorAt(point(0, 1, 0)))
            .isEqualTo(Color.WHITE)
        assertThatColor(pattern.colorAt(point(0, 2, 0)))
            .isEqualTo(Color.WHITE)
    }

    @Test
    fun `A stripe pattern is constant in z` () {
        val pattern = stripePattern()
        assertThatColor(pattern.colorAt(point(0, 0, 0)))
            .isEqualTo(Color.WHITE)
        assertThatColor(pattern.colorAt(point(0, 0, 1)))
            .isEqualTo(Color.WHITE)
        assertThatColor(pattern.colorAt(point(0, 0, 2)))
            .isEqualTo(Color.WHITE)
    }

    @Test
    fun `A stripe pattern alternates in x` () {
        val pattern = stripePattern()
        assertThatColor(pattern.colorAt(point(0, 0, 0)))
            .isEqualTo(Color.WHITE)
        assertThatColor(pattern.colorAt(point(0.9, 0, 0)))
            .isEqualTo(Color.WHITE)
        assertThatColor(pattern.colorAt(point(1, 0, 0)))
            .isEqualTo(Color.BLACK)
        assertThatColor(pattern.colorAt(point(-0.1, 0, 0)))
            .isEqualTo(Color.BLACK)
        assertThatColor(pattern.colorAt(point(-1, 0, 0)))
            .isEqualTo(Color.BLACK)
        assertThatColor(pattern.colorAt(point(-1.1, 0, 0)))
            .isEqualTo(Color.WHITE)
    }

    @Test
    fun `Stripes with an object transformation` () {
        val obj = sphere(transform = scaling(2, 2, 2))
        val pattern = stripePattern()
        assertThatColor(pattern.patternAtShape(obj, point(1.5, 0, 0)))
            .isEqualTo(Color.WHITE)
    }

    @Test
    fun `Stripes with a pattern transformation` () {
        val obj = sphere()
        val pattern = stripePattern(transform = scaling(2, 2, 2))
        assertThatColor(pattern.patternAtShape(obj, point(1.5, 0, 0)))
            .isEqualTo(Color.WHITE)
    }

    @Test
    fun `Stripes with both an object and a pattern transformation` () {
        val obj = sphere(transform = scaling(2, 2, 2))
        val pattern = stripePattern(transform = translation(0.5, 0, 0))
        assertThatColor(pattern.patternAtShape(obj, point(2.5, 0, 0)))
            .isEqualTo(Color.WHITE)
    }

    @Test
    fun `The default pattern transformation` () {
        val pattern = testPattern()
        assertThatNDArray(pattern.transform).isIdentity()
    }

    @Test
    fun `Assigning a transformation` () {
        val pattern = testPattern(transform = translation(1, 2, 3))
        assertThatNDArray(pattern.transform).isEqualTo(translation(1, 2, 3))
    }

    @Test
    fun `A pattern with an object transformation` () {
        val shape = sphere(transform = scaling(2, 2, 2))
        val pattern = testPattern()
        assertThatColor(pattern.patternAtShape(shape, point(2, 3, 4)))
            .isEqualTo(color(1, 1.5, 2))
    }

    @Test
    fun `A pattern with a pattern transformation` () {
        val shape = sphere()
        val pattern = testPattern(transform = scaling(2, 2, 2))
        assertThatColor(pattern.patternAtShape(shape, point(2, 3, 4)))
            .isEqualTo(color(1, 1.5, 2))
    }

    @Test
    fun `A pattern with both an object and a pattern transformation` () {
        val shape = sphere(transform = scaling(2, 2, 2))
        val pattern = testPattern(transform = translation(0.5, 1, 1.5))
        assertThatColor(pattern.patternAtShape(shape, point(2.5, 3, 3.5)))
            .isEqualTo(color(0.75, 0.5, 0.25))
    }

    @Test
    fun `A gradient linearly interpolates between colors` () {
        val pattern = gradientPattern()
        assertThatColor(pattern.colorAt(point(0, 0, 0))).isEqualTo(Color.WHITE)
        assertThatColor(pattern.colorAt(point(0.25, 0, 0))).isEqualTo(color(0.75, 0.75, 0.75))
        assertThatColor(pattern.colorAt(point(0.5, 0, 0))).isEqualTo(color(0.5, 0.5, 0.5))
        assertThatColor(pattern.colorAt(point(0.75, 0, 0))).isEqualTo(color(0.25, 0.25, 0.25))
    }

    @Test
    fun `A ring should extend in both x and z` () {
        val pattern = ringPattern()
        assertThatColor(pattern.colorAt(point(0, 0, 0))).isEqualTo(Color.WHITE)
        assertThatColor(pattern.colorAt(point(1, 0, 0))).isEqualTo(Color.BLACK)
        assertThatColor(pattern.colorAt(point(0, 0, 1))).isEqualTo(Color.BLACK)
        assertThatColor(pattern.colorAt(point(0.708, 0, 0.708))).isEqualTo(Color.BLACK)
    }

    @Test
    fun `Checkers should repeat in x` () {
        val pattern = checkersPattern()
        assertThatColor(pattern.colorAt(point(0, 0, 0))).isEqualTo(Color.WHITE)
        assertThatColor(pattern.colorAt(point(0.99, 0, 0))).isEqualTo(Color.WHITE)
        assertThatColor(pattern.colorAt(point(1.01, 0, 0))).isEqualTo(Color.BLACK)
    }

    @Test
    fun `Checkers should repeat in y` () {
        val pattern = checkersPattern()
        assertThatColor(pattern.colorAt(point(0, 0, 0))).isEqualTo(Color.WHITE)
        assertThatColor(pattern.colorAt(point(0, 0.99, 0))).isEqualTo(Color.WHITE)
        assertThatColor(pattern.colorAt(point(0, 1.01, 0))).isEqualTo(Color.BLACK)
    }

    @Test
    fun `Checkers should repeat in z` () {
        val pattern = checkersPattern()
        assertThatColor(pattern.colorAt(point(0, 0, 0))).isEqualTo(Color.WHITE)
        assertThatColor(pattern.colorAt(point(0, 0, 0.99))).isEqualTo(Color.WHITE)
        assertThatColor(pattern.colorAt(point(0, 0, 1.01))).isEqualTo(Color.BLACK)
    }

    fun testPattern(transform: NDArray = ID4): TestPattern = TestPattern(transform)
}