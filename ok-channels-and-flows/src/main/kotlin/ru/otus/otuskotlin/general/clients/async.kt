package ru.otus.otuskotlin.general.clients

import kotlinx.coroutines.delay

interface AsyncDetectorClient {
    suspend fun open()
    suspend fun readSerialNumber(): String
    suspend fun readValue(): Double
    suspend fun close()
}

class RandomizedAsyncDetectorClient(
    val serialNumber: String,
    sampleDistribution: Sequence<Double>
) : AsyncDetectorClient {
    private val sampleIterator = sampleDistribution.iterator()

    override suspend fun open() {
        println("Sending command asynchronously to start measurement")
        delay(5)
        println("Client opened")
    }

    override suspend fun readSerialNumber(): String {
        delay(5)
        return serialNumber
    }

    override suspend fun readValue(): Double {
        delay(5)
        return sampleIterator.next()
    }

    override suspend fun close() {
        println("Sending command asynchronously to stop measuring")
        delay(5)
        println("Client closed")
    }
}