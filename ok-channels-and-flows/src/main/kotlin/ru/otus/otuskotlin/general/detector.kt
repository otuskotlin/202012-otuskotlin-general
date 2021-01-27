package ru.otus.otuskotlin.general

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.isActive
import ru.otus.otuskotlin.general.clients.AsyncDetectorClient
import ru.otus.otuskotlin.general.clients.BlockingDetectorClient
import ru.otus.otuskotlin.general.clients.CallbackDetectorClient

data class Sample(
    val serialNumber: String,
    val timestamp: Long,
    val value: Double
)

interface Detector {
    fun samples(): Flow<Sample>
}

class AsyncDetector(
    private val client: AsyncDetectorClient,
    private val samplePeriod: Long
) : Detector {
    override fun samples(): Flow<Sample> = flow {
        client.open()
        try {
            val serialNumber = client.readSerialNumber()
            while (currentCoroutineContext().isActive) {
                val value = client.readValue()
                emit(Sample(serialNumber, System.currentTimeMillis(), value))
                delay(samplePeriod)
            }
        } finally {
            client.close()
        }
    }
}

class BlockingDetector(
    private val client: BlockingDetectorClient,
    private val samplePeriod: Long
) : Detector {
    override fun samples(): Flow<Sample> = flow {
        client.open()
        try {
            val serialNumber = client.readSerialNumber()
            while (currentCoroutineContext().isActive) {
                val value = client.readValue()
                emit(Sample(serialNumber, System.currentTimeMillis(), value))
                delay(samplePeriod)
            }
        } finally {
            client.close()
        }
    }.flowOn(Dispatchers.IO)
}

class CallbackDetector(
    private val client: CallbackDetectorClient,
    private val samplePeriod: Long
) : Detector {
    override fun samples(): Flow<Sample> = callbackFlow<Sample> {
        val listener = object : CallbackDetectorClient.Listener {
            lateinit var serialNumber: String

            override fun onOpened(serialNumber: String) {
                this.serialNumber = serialNumber
            }

            override fun onValueRead(value: Double) {
                offer(Sample(serialNumber, System.currentTimeMillis(), value))
            }

            override fun onClosed() {
                close()
            }
        }

        val handle = client.open(listener)
        awaitClose { handle.close() }
    }.sample(samplePeriod)
}