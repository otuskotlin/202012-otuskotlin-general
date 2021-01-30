package ru.otus.otuskotlin.general.multiplatform

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.test.Test
import kotlin.test.assertEquals

internal class SharedClassTestJs {
    @Test
    fun sharedClass() {
        val sc = SharedClass()
        GlobalScope.launch {
            val res = sc.request("one")
            assertEquals("Js done", res)
        }
    }
}
