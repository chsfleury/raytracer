package fr.chsfleury.raytracer

import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream

fun args(vararg args: Any?): Arguments = Arguments.of(*args)

fun cases(vararg cases: Arguments): Stream<Arguments> = Stream.of(*cases)