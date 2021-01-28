package cps_intercepted

import java.util.*
import java.util.concurrent.BlockingQueue
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread

object StepByStepDispatcher : ContinuationInterceptor {
    private val computationSteps: Queue<ComputationStep<*>> = ConcurrentLinkedQueue()

    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> =
        object : Continuation<T> by continuation {
            override fun resumeWith(result: T) {
                val step = ComputationStep(continuation, result)
                println("Suspending: $step")
                computationSteps.add(step)
            }
        }

    fun dispatchNext() {
        val step = computationSteps.poll() ?: return
        step.resume()
    }

    private data class ComputationStep<T>(
        val continuation: Continuation<T>,
        val result: T
    ) {
        fun resume() {
            println("  Resuming: $this")
            continuation.resumeWith(result)
        }
    }
}

object MultiThreadedDispatcher : ContinuationInterceptor {
    private val computationSteps: BlockingQueue<ComputationStep<*>> = LinkedBlockingQueue()

    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> =
        object : Continuation<T> by continuation {
            override fun resumeWith(result: T) {
                val step = ComputationStep(continuation, result)
                println("Suspending on ${Thread.currentThread().name}: $step")
                computationSteps.put(step)
            }
        }

    fun startWorkers(parallelism: Int) {
        repeat(parallelism) {
            thread {
                while (!Thread.interrupted()) {
                    computationSteps.take().resume()
                }
            }
        }
    }

    data class ComputationStep<T>(
        val continuation: Continuation<T>,
        val result: T
    ) {
        fun resume() {
            println("  Resuming on ${Thread.currentThread().name}: $this")
            continuation.resumeWith(result)
        }
    }
}