package ru.otus.otuskotlin.general

import kotlinx.coroutines.flow.Flow
import org.intellij.lang.annotations.Language

interface Connection {
    fun query(@Language("SQL") text: String, vararg params: Any): Result
}

interface Result : Flow<Row> {
    suspend fun modifiedCount(): Int
}

interface Row {
    operator fun get(index: Int): Any
    operator fun get(name: String): Any
}