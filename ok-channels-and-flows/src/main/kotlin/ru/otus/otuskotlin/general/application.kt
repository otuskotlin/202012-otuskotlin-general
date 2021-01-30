package ru.otus.otuskotlin.general

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import ru.otus.otuskotlin.general.clients.AsyncDetectorClient
import ru.otus.otuskotlin.general.clients.BlockingDetectorClient
import ru.otus.otuskotlin.general.clients.CallbackDetectorClient
import ru.otus.otuskotlin.general.clients.SampleType
import ru.otus.otuskotlin.general.clients.detectorClients
import ru.otus.otuskotlin.general.clients.detectorsLocations
import ru.otus.otuskotlin.general.clients.detectorsSampleTypes
import java.io.File
import java.io.FileWriter
import java.text.DecimalFormat

const val SAMPLE_PERIOD = 1000L

data class SampleRecord(
    val sample: Sample,
    val type: SampleType,
    val location: String
)

data class LocationRecord(
    val location: String,
    val timestamp: Long,
    val temperature: Double = Double.NaN,
    val humidity: Double = Double.NaN,
    val pressure: Double = Double.NaN
)

suspend fun Flow<SampleRecord>.saveToCsv(file: File) {
    val fileWriter = withContext(Dispatchers.IO) { FileWriter(file) }
    try {
        collect { record ->
            withContext(Dispatchers.IO) {
                fileWriter.append(record.sample.serialNumber)
                fileWriter.append(';')
                fileWriter.append(record.sample.timestamp.toString())
                fileWriter.append(';')
                fileWriter.append(record.sample.value.toString())
                fileWriter.append(';')
                fileWriter.append(record.type.toString())
                fileWriter.append(';')
                fileWriter.append(record.location)
                fileWriter.appendLine()
            }
        }
    } finally {
        withContext(Dispatchers.IO) { fileWriter.close() }
    }
}

fun Flow<SampleRecord>.groupByLocation(): Flow<LocationRecord> = flow {
    val locationsRecords = mutableMapOf<String, LocationRecord>()
    collect { sampleRecord ->
        val record = locationsRecords.compute(sampleRecord.location) { location, record ->
            val value = sampleRecord.sample.value
            val timestamp = sampleRecord.sample.timestamp
            when (sampleRecord.type) {
                SampleType.TEMPERATURE ->
                    record?.copy(timestamp = timestamp, temperature = value)
                        ?: LocationRecord(location, timestamp = timestamp, temperature = value)
                SampleType.HUMIDITY ->
                    record?.copy(timestamp = timestamp, humidity = value)
                        ?: LocationRecord(location, timestamp = timestamp, humidity = value)
                SampleType.PRESSURE ->
                    record?.copy(timestamp = timestamp, pressure = value)
                        ?: LocationRecord(location, timestamp = timestamp, pressure = value)
            }
        }
        emit(record!!)
    }
}

fun main(): Unit = runBlocking {
    val detectors = detectorClients
        .mapNotNull { client ->
            when (client) {
                is AsyncDetectorClient -> AsyncDetector(client, SAMPLE_PERIOD)
                is BlockingDetectorClient -> BlockingDetector(client, SAMPLE_PERIOD)
                is CallbackDetectorClient -> CallbackDetector(client, SAMPLE_PERIOD)
                else -> null
            }
        }

    val samples = detectors
        .map { it.samples() }
        .merge()

    val sampleRecords = samples
        .mapNotNull {
            val sampleType = detectorsSampleTypes[it.serialNumber]
            val location = detectorsLocations[it.serialNumber]
            when {
                sampleType == null -> null
                location == null -> null
                else -> SampleRecord(it, sampleType, location)
            }
        }
        .shareIn(this, SharingStarted.Lazily)

    val format = DecimalFormat("#,###.00")
    sampleRecords
        .groupByLocation()
        .onEach {
            println(
                "${it.timestamp}: ${it.location.padStart(10)}" +
                        "    t=${format.format(it.temperature).padStart(6)}" +
                        "    H=${format.format(it.humidity).padStart(6)}" +
                        "    P=${format.format(it.pressure).padStart(9)}"
            )
        }
        .launchIn(this)

    sampleRecords.saveToCsv(File("samples.csv"))
}