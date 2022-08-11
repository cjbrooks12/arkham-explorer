package com.copperleaf.arkham.models.api

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = ChaosTokenSerializer::class)
sealed interface ChaosToken

data class NumberToken(val value: Int) : ChaosToken {
    override fun toString(): String {
        return when {
            value > 0 -> "+${value}"
            value == 0 -> "0"
            value < 0 -> "$value"
            else -> error("nope")
        }
    }
}

object Skull : ChaosToken {
    override fun toString(): String = "Skull"
}

object Cultist : ChaosToken {
    override fun toString(): String = "Cultist"
}

object Tablet : ChaosToken {
    override fun toString(): String = "Tablet"
}

object ElderThing : ChaosToken {
    override fun toString(): String = "Elder Thing"
}

object AutoFail : ChaosToken {
    override fun toString(): String = "Auto-Fail"
}

object ElderSign : ChaosToken {
    override fun toString(): String = "Elder Sign"
}

object Bless : ChaosToken {
    override fun toString(): String = "Bless"
}

object Curse : ChaosToken {
    override fun toString(): String = "Curse"
}

@Serializable(with = ChaosBagDifficultySerializer::class)
enum class ChaosBagDifficulty(val value: String) {
    Easy("easy"), Standard("standard"), Hard("hard"), Expert("expert")
}

@Serializable
data class ScenarioChaosBag(
    val difficulty: ChaosBagDifficulty,
    val tokens: List<ChaosToken>,
)

object ChaosTokenSerializer : KSerializer<ChaosToken> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ChaosToken", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ChaosToken) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): ChaosToken {
        return when (val stringValue = decoder.decodeString()) {
            "Skull" -> Skull
            "Cultist" -> Cultist
            "Tablet" -> Tablet
            "Elder Thing" -> ElderThing
            "Auto-Fail" -> AutoFail
            "Elder Sign" -> ElderSign
            "Bless" -> Bless
            "Curse" -> Curse
            else -> {
                val numberValue = stringValue.toIntOrNull()
                check(numberValue != null) { "${stringValue} is not a valid Chaos Token" }
                NumberToken(numberValue)
            }
        }
    }
}


object ChaosBagDifficultySerializer : KSerializer<ChaosBagDifficulty> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ChaosBagDifficulty", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ChaosBagDifficulty) {
        encoder.encodeString(value.value)
    }

    override fun deserialize(decoder: Decoder): ChaosBagDifficulty {
        val stringValue = decoder.decodeString()
        return ChaosBagDifficulty
            .values()
            .singleOrNull { it.value == stringValue }
            ?: error("${stringValue} is not a valid Chaos Bag difficulty")
    }
}
