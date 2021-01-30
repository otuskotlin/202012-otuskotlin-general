package ru.otus.otuskotlin.general.multiplatform

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

internal class SharedClassTestLinux {
    @Test
    fun sharedClass() = runBlocking<Unit> {
        val sc = SharedClass()
        val res = sc.request("one")
        assertEquals("Linux done", res)
    }
}
