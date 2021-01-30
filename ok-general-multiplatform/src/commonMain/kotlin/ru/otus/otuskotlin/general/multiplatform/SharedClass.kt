package ru.otus.otuskotlin.general.multiplatform

expect class SharedClass {
    suspend fun request(id: String): String
}
