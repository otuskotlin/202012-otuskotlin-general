package cps

interface Context

object EmptyContext : Context

interface Continuation<T> {
    val context: Context
    fun resumeWith(result: T)
}

fun <T> Context.newContinuation(resumeWith: Context.(T) -> Unit): Continuation<T> =
    object : Continuation<T> {
        override val context: Context = this@newContinuation
        override fun resumeWith(result: T) = this@newContinuation.resumeWith(result)
    }

fun sum(a: Double, b: Double, continuation: Continuation<Double>) =
    continuation.resumeWith(a + b)

fun times(a: Double, b: Double, continuation: Continuation<Double>) =
    continuation.resumeWith(a * b)

fun sqrt(a: Double, continuation: Continuation<Double>) =
    continuation.resumeWith(kotlin.math.sqrt(a))

fun l2norm(x: Double, y: Double, continuation: Continuation<Double>) =
    with(continuation.context) {
        times(x, x, newContinuation { x_square ->
            times(y, y, newContinuation { y_square ->
                sum(x_square, y_square, newContinuation { squares_sum ->
                    sqrt(squares_sum, continuation)
                })
            })
        })
    }

fun main() {
    with(EmptyContext) {
        l2norm(2.0, 3.0, newContinuation { print("L2-norm: $it") })
    }
}