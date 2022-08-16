package com.copperleaf.arkham.random

import kotlin.random.Random


/**
 * Denotes an instance of [Random] that does not mutate its own internal state. Unlike standard [Random],
 * [ImmutableRandom] will always return the same value for subsequent calls to [nextBits], [nextInt], etc. One must
 * call [nextRandom] to get a new instance of [ImmutableRandom] with a state that would typically have been updated
 * internally.
 *
 * For the same seed, any sequence of `next*(), nextRandom()` should yield the same result as a normal [Random] calling
 * the same sequence of `next*
 */
abstract class ImmutableRandom : Random() {

    abstract fun nextRandom(): ImmutableRandom
}
