package cps_intercepted

import java.util.*

interface Context {
    val interceptor: ContinuationInterceptor?
}

object EmptyContext : Context {
    override val interceptor: ContinuationInterceptor? = null
}

fun contextOf(interceptor: ContinuationInterceptor): Context =
    object : Context {
        override val interceptor: ContinuationInterceptor = interceptor
    }

interface Continuation<T> {
    val context: Context
    fun resumeWith(result: T)
}

interface ContinuationInterceptor {
    fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T>
}

fun <T> Context.newContinuation(resumeWith: Context.(T) -> Unit): Continuation<T> {
    val continuation = object : Continuation<T> {
        override val context: Context = this@newContinuation
        override fun resumeWith(result: T) = this@newContinuation.resumeWith(result)
    }
    return interceptor?.interceptContinuation(continuation) ?: continuation
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
    with(contextOf(StepByStepDispatcher)) {
        l2norm(2.0, 3.0, newContinuation { println("L2-norm: $it") })
    }
    StepByStepDispatcher.dispatchNext()
    StepByStepDispatcher.dispatchNext()
    StepByStepDispatcher.dispatchNext()
    StepByStepDispatcher.dispatchNext()

//    MultiThreadedDispatcher.startWorkers(2)
//    with(contextOf(MultiThreadedDispatcher)) {
//        l2norm(2.0, 3.0, newContinuation { print("L2-norm: $it") })
//    }
}