package ru.otus.otuskotlin.general.multiplatform

import kotlin.test.Test
import kotlin.test.assertEquals

internal class SharedModelTestJvm {
    @Test
    fun shmTest() {
        assertEquals("my name", SharedModel().`my name`)
    }
}
