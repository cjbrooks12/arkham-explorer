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
import kotlin.time.Duration.Companion.seconds

@Serializable
data class HttpMetadata(
    @Serializable(with = DurationSerializer::class)
    val maxAge: Duration,
    val lastFetched: Instant,
    val clean: Boolean,
) {
    fun isExpired(): Boolean {
        val currentTime = Clock.System.now()

        if (BuildConfig.DEBUG) {
            // in debug, make the max age last significantly longer, to save on time and protect the target servers from
            // too much traffic
            return currentTime > (lastFetched + (maxAge * 10 * 12)) // turns 10 minute cache into 12 hours
        } else {
            // in production, obey the max-age header exactly
            return currentTime > (lastFetched + maxAge)
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
