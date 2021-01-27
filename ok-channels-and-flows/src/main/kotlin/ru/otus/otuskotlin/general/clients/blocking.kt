package ru.otus.otuskotlin.general.clients

import java.io.Closeable

interface BlockingDetectorClient : Closeable {
    fun open()
    fun readSerialNumber(): String
    fun readValue(): Double
}

class RandomizedBlockingDetectorClient(
    val serialNumber: String,
    sampleDistribution: Sequence<Double>
) : BlockingDetectorClient {
    private val sampleIterator = sampleDistribution.iterator()

    override fun open() {
        println("Sending command asynchronously to start measurement")
        Thread.sleep(5)
        println("Client opened")
    }

    override fun readSerialNumber(): String {
        Thread.sleep(5)
        return serialNumber
    }

    override fun readValue(): Double {
        Thread.sleep(5)
        return sampleIterator.next()
    }

    override fun close() {
        println("Sending command to stop measuring")
        Thread.sleep(5)
        println("Client closed")
    }
}