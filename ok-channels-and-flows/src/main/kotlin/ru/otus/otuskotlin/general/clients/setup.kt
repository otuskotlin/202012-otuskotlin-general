package ru.otus.otuskotlin.general.clients

val asyncFTemperatureDetectorClient = RandomizedAsyncDetectorClient(
    serialNumber = "temp_f_async",
    sampleDistribution = TEMPERATURE_DISTRIBUTION
)

val blockingCTemperatureDetectorClient = RandomizedBlockingDetectorClient(
    serialNumber = "temp_c_blocking",
    sampleDistribution = TEMPERATURE_DISTRIBUTION
)

val callbackHumidityDetectorClient = RandomizedCallbackDetectorClient(
    serialNumber = "humidity_callback_high_rate",
    sampleDistribution = HUMIDITY_DISTRIBUTION,
    samplePeriod = 50
)

val asyncHumidityDetectorClient = RandomizedAsyncDetectorClient(
    serialNumber = "humidity_async",
    sampleDistribution = HUMIDITY_DISTRIBUTION
)

val blockingPressureDetectorClient = RandomizedBlockingDetectorClient(
    serialNumber = "pressure_blocking_noisy",
    sampleDistribution = NOISY_PRESSURE_DISTRIBUTION
)

val callbackPressureDetectorClient = RandomizedCallbackDetectorClient(
    serialNumber = "presure_callback",
    sampleDistribution = PRESSURE_DISTRIBUTION,
    samplePeriod = 1000
)

val detectorClients = listOf<Any>(
    asyncFTemperatureDetectorClient,
    blockingCTemperatureDetectorClient,
    callbackHumidityDetectorClient,
    asyncHumidityDetectorClient,
    blockingPressureDetectorClient,
    callbackPressureDetectorClient
)

enum class SampleType {
    TEMPERATURE,
    HUMIDITY,
    PRESSURE
}

val detectorsSampleTypes = mapOf(
    asyncFTemperatureDetectorClient.serialNumber to SampleType.TEMPERATURE,
    blockingCTemperatureDetectorClient.serialNumber to SampleType.TEMPERATURE,
    callbackHumidityDetectorClient.serialNumber to SampleType.HUMIDITY,
    asyncHumidityDetectorClient.serialNumber to SampleType.HUMIDITY,
    blockingPressureDetectorClient.serialNumber to SampleType.PRESSURE,
    callbackPressureDetectorClient.serialNumber to SampleType.PRESSURE
)

const val LOCATION_ARBAT = "Arbat"
const val LOCATION_RED_SQUARE = "Red Square"

val detectorsLocations = mapOf(
    asyncFTemperatureDetectorClient.serialNumber to LOCATION_ARBAT,
    blockingCTemperatureDetectorClient.serialNumber to LOCATION_RED_SQUARE,
    callbackHumidityDetectorClient.serialNumber to LOCATION_ARBAT,
    asyncHumidityDetectorClient.serialNumber to LOCATION_RED_SQUARE,
    blockingPressureDetectorClient.serialNumber to LOCATION_ARBAT,
    callbackPressureDetectorClient.serialNumber to LOCATION_RED_SQUARE
)