package fr.chsfleury.raytracer.core

object PairUtils {

    fun Pair<Double, Double>.asc(): Pair<Double, Double> = if (first > second) {
        swap()
    } else {
        this
    }

    fun Pair<Double, Double>.swap(): Pair<Double, Double> = Pair(second, first)

}