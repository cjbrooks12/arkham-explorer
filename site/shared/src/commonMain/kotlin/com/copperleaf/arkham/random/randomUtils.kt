package com.copperleaf.arkham.random

internal fun Int.takeUpperBits(bitCount: Int): Int =
    this.ushr(32 - bitCount) and (-bitCount).shr(31)

public fun ImmutableRandom(seed: Int): ImmutableRandom = ImmutableRandom(seed, seed.shr(31))

public fun ImmutableRandom(seed: Long): ImmutableRandom = ImmutableRandom(seed.toInt(), seed.shr(32).toInt())

public fun ImmutableRandom(seed1: Int, seed2: Int): ImmutableRandom {
    var initialInstance = XorWowImmutableRandom(seed1, seed2, 0, 0, seed1.inv(), (seed1 shl 10) xor (seed2 ushr 4))

    with(initialInstance) {
        require((_x or _y or _z or _w or _v) != 0) { "Initial state must have at least one non-zero element." }
    }

    // some trivial seeds can produce several values with zeroes in upper bits, so we discard first 64
    repeat(64) { initialInstance = initialInstance.nextRandom() }

    return initialInstance
}
