package ru.otus.otuskotlin.general.multiplatform

import kotlinx.coroutines.delay

actual class SharedClass {
    actual suspend fun request(id: String): String {
        delay(1000)
        return "Linux done"
    }
}
