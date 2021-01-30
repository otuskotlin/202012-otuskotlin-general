package ru.otus.otuskotlin.general.multiplatform

import kotlin.test.Test
import kotlin.test.assertTrue

class SomeFunTestLinux {
    @Test
    fun someFunTest() {
        val str = "someStr"
        assertTrue {
            someFun(str).contains("Linux")
        }
    }
}
