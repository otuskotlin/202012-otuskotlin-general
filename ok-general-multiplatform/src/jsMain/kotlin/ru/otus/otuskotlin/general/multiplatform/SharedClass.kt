package ru.otus.otuskotlin.general.multiplatform

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.promise
import kotlin.js.Promise

actual class SharedClass {
    actual suspend fun request(id: String): String {
        delay(1000)
        return "Js done"
    }

    fun requestPromised(id: String): Promise<String> = GlobalScope.promise {
        request(id)
    }
}
