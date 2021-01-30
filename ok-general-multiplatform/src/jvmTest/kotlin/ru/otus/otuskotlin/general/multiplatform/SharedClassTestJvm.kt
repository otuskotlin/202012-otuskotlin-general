package ru.otus.otuskotlin.general.multiplatform

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

internal class SharedClassTestJvm {
    @Test
    fun sharedClass() = runBlocking<Unit> {
        val sc = SharedClass()
        val res = sc.request("one")
        assertEquals("Jvm done", res)
    }
}
