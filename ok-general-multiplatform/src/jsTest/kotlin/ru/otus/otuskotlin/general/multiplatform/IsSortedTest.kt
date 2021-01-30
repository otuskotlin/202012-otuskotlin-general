package ru.otus.otuskotlin.general.multiplatform

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class IsSortedTest {
    @Test
    fun isSortedFalseTest() {
        val array = arrayOf(1,5,3,7)
        assertFalse(sorted(array))
    }

    @Test
    fun isSortedTrueTest() {
        val array = arrayOf(1,2,3,4)
        assertTrue(sorted(array))
    }
}
