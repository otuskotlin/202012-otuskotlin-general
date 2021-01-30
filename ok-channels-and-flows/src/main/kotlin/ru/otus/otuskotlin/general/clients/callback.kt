package ru.otus.otuskotlin.general.clients

import java.io.Closeable
import java.util.*
import kotlin.concurrent.schedule
import kotlin.concurrent.scheduleAtFixedRate

interface CallbackDetectorClient {
    fun open(listener: Listener): Closeable

    interface Listener {
        fun onOpened(serialNumber: String)
        fun onValueRead(value: Double)
        fun onClosed()
    }
}

class RandomizedCallbackDetectorClient(
    val serialNumber: String,
    sampleDistribution: Sequence<Double>,
    private val samplePeriod: Long
) : CallbackDetectorClient {
    private val sampleIterator = sampleDistribution.iterator()

    override fun open(listener: CallbackDetectorClient.Listener): Closeable {
        val timer = Timer()
        println("Sending command asynchronously to start measurement")
        timer.schedule(5) {
            println("Client opened")
            listener.onOpened(serialNumber)

            timer.scheduleAtFixedRate(5, samplePeriod) {
                listener.onValueRead(sampleIterator.next())
            }
        }

        return Closeable {
            println("Sending command to stop measuring")
            timer.schedule(5) {
                timer.cancel()
                println("Closing api")
                listener.onClosed()
            }
        }
    }
}