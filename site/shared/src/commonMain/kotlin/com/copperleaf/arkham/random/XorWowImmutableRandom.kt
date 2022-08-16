package com.copperleaf.arkham.random

/**
 * Random number generator, using Marsaglia's "xorwow" algorithm
 *
 * Cycles after 2^192 - 2^32 repetitions.
 *
 * For more details, see Marsaglia, George (July 2003). "Xorshift RNGs". Journal of Statistical Software. 8 (14). doi:10.18637/jss.v008.i14
 *
 * Available at https://www.jstatsoft.org/v08/i14/paper
 *
 * This is an exact copy of the default [kotlin.random.XorWowRandom], but where its internal state is immutable. One
 * must call [ImmutableRandom.nextRandom] to get an instance that will return the next step in the sequence.
 *
 */
internal class XorWowImmutableRandom internal constructor(
    internal val _x: Int,
    internal val _y: Int,
    internal val _z: Int,
    internal val _w: Int,
    internal val _v: Int,
    internal val _addend: Int
) : ImmutableRandom() {

    override fun nextInt(): Int {
        // Equivalent to the xorxow algorithm
        // From Marsaglia, G. 2003. Xorshift RNGs. J. Statis. Soft. 8, 14, p. 5
        var t = _x
        t = t xor (t ushr 2)
        val x = _y
        val y = _z
        val z = _w
        val v0 = _v
        val w = v0
        t = (t xor (t shl 1)) xor v0 xor (v0 shl 4)
        val v = t
        val addend = _addend + 362437
        return t + addend
    }

    override fun nextBits(bitCount: Int): Int =
        nextInt().takeUpperBits(bitCount)

    override fun nextRandom(): XorWowImmutableRandom {
        // Equivalent to the xorxow algorithm
        // From Marsaglia, G. 2003. Xorshift RNGs. J. Statis. Soft. 8, 14, p. 5
        var t = _x
        t = t xor (t ushr 2)
        val x = _y
        val y = _z
        val z = _w
        val v0 = _v
        val w = v0
        t = (t xor (t shl 1)) xor v0 xor (v0 shl 4)
        val v = t
        val addend = _addend + 362437
        return XorWowImmutableRandom(
            _x = x,
            _y = y,
            _z = z,
            _w = w,
            _v = v,
            _addend = addend,
        )
    }
}
