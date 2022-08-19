package com.copperleaf.arkham.random

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class TestImmutableRandom {

    @Test
    fun testActuallyImmutable() {
        val standardRandom = Random(0)
        val immutableRandom = ImmutableRandom(0)

        assertEquals(-1934310868, standardRandom.nextInt())
        assertEquals(1409199696, standardRandom.nextInt())
        assertEquals(-649160781, standardRandom.nextInt())
        assertEquals(-1454478562, standardRandom.nextInt())

        assertEquals(-1934310868, immutableRandom.nextInt())
        assertEquals(-1934310868, immutableRandom.nextInt())
        assertEquals(-1934310868, immutableRandom.nextInt())
        assertEquals(-1934310868, immutableRandom.nextInt())
    }

    @Test
    fun testSameSequenceAsStandardRandom() {
        val standardRandom = Random(0)
        val immutableRandom = ImmutableRandom(0)

        val standardRandomSequence: List<Int> = (0..1000).map {
            standardRandom.nextInt()
        }
        val immutableRandomSequence: List<Int> = (0..1000)
            .runningFold(immutableRandom to 0) { acc, _ -> acc.first.nextRandom() to acc.first.nextInt() }
            .map { it.second }
            .drop(1)

        assertEquals(standardRandomSequence, immutableRandomSequence)
    }
}
