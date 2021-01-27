package ru.otus.otuskotlin.general.clients

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

fun normalDistributedSequence(mean: Double, std: Double): Sequence<Double> =
    TODO("Produce sequence of normal distribute doubles")

fun boxMullerRandom(): Pair<Double, Double> {
    // Box-Muller algorithm to produce normal distributed numbers with mean = 0, std = 1
    // https://en.wikipedia.org/wiki/Box%E2%80%93Muller_transform
    val u1 = Random.nextDouble()
    val u2 = Random.nextDouble()
    val z1 = sqrt(-2 * ln(u1)) * cos(2 * PI * u2)
    val z2 = sqrt(-2 * ln(u1)) * sin(2 * PI * u2)
    return z1 to z2
}

val TEMPERATURE_DISTRIBUTION = normalDistributedSequence(19.2, 1.0)
val HUMIDITY_DISTRIBUTION = normalDistributedSequence(45.0, 5.0)
val PRESSURE_DISTRIBUTION = normalDistributedSequence(987.5, 9.0)
val PRESSURE_NOISE = normalDistributedSequence(0.0, 100.0)
val NOISY_PRESSURE_DISTRIBUTION = TODO("Combine PRESSURE_DISTRIBUTION with PRESSURE_NOISE by elements")