package cps

import java.util.*
import kotlin.concurrent.schedule

fun delay(millis: Long, continuation: Continuation<Unit>) {
    val timer = Timer()
    timer.schedule(millis) {
        timer.cancel()
        continuation.resumeWith(Unit)
    }
}