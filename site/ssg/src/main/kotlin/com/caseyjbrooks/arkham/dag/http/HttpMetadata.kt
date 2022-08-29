package com.caseyjbrooks.arkham.dag.http

import com.caseyjbrooks.arkham.site.BuildConfig
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds

@Serializable
data class HttpMetadata(
    @Serializable(with = DurationSerializer::class)
    val maxAge: Duration,
    val lastFetched: Instant,
    val clean: Boolean,
) {
    fun isExpired(): Boolean {
        if (maxAge > 0.seconds) {
            // we had a max-age header
            val currentTime = Clock.System.now()

            if (BuildConfig.DEBUG) {
                // in debug, make the max age last significantly longer, to save on time and protect the target servers from
                // too much traffic
                return currentTime > (lastFetched + (maxAge * 6 * 12)) // turns 10 minute cache into 12 hours
            } else {
                // in production, obey the max-age header exactly
                return currentTime > (lastFetched + maxAge)
            }
        } else {
            // we did not have a max-age header
            val currentTime = Clock.System.now()

            if (BuildConfig.DEBUG) {
                // in debug, cache it for 12 hours
                return currentTime > (lastFetched + 12.hours)
            } else {
                // in production, since we did not receive the header, it is always dirty
                return true
            }
        }
    }
}

object DurationSerializer : KSerializer<Duration> {
    private val delegateSerializer = Int.serializer()
    override val descriptor: SerialDescriptor = delegateSerializer.descriptor

    override fun deserialize(decoder: Decoder): Duration {
        val int = delegateSerializer.deserialize(decoder)
        return int.seconds
    }

    override fun serialize(encoder: Encoder, value: Duration) {
        delegateSerializer.serialize(encoder, value.inWholeSeconds.toInt())
    }
}
