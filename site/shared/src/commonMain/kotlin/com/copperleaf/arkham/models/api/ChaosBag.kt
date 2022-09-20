package com.copperleaf.arkham.models.api

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = ChaosTokenSerializer::class)
sealed interface ChaosToken {
    fun modifierValue(
        referenceCard: ScenarioReferenceCard?,
        variableValues: Map<String, Int>,
    ): Int?
}

data class NumberToken(private val value: Int) : ChaosToken {
    override fun modifierValue(
        referenceCard: ScenarioReferenceCard?,
        variableValues: Map<String, Int>,
    ): Int? {
        return value
    }

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

    override fun modifierValue(
        referenceCard: ScenarioReferenceCard?,
        variableValues: Map<String, Int>,
    ): Int? {
        return referenceCard?.tokens?.single { it.token == this }?.modifierValue?.let {
            when(it) {
                is ChaosTokenModifierValue.Static -> it.value
                is ChaosTokenModifierValue.Variable -> variableValues[it.key]?.times(-1)
            }
        }
    }
}

object Cultist : ChaosToken {
    override fun toString(): String = "Cultist"

    override fun modifierValue(
        referenceCard: ScenarioReferenceCard?,
        variableValues: Map<String, Int>,
    ): Int? {
        return referenceCard?.tokens?.single { it.token == this }?.modifierValue?.let {
            when(it) {
                is ChaosTokenModifierValue.Static -> it.value
                is ChaosTokenModifierValue.Variable -> variableValues[it.key]?.times(-1)
            }
        }
    }
}

object Tablet : ChaosToken {
    override fun toString(): String = "Tablet"

    override fun modifierValue(
        referenceCard: ScenarioReferenceCard?,
        variableValues: Map<String, Int>,
    ): Int? {
        return referenceCard?.tokens?.single { it.token == this }?.modifierValue?.let {
            when(it) {
                is ChaosTokenModifierValue.Static -> it.value
                is ChaosTokenModifierValue.Variable -> variableValues[it.key]?.times(-1)
            }
        }
    }
}

object ElderThing : ChaosToken {
    override fun toString(): String = "Elder Thing"

    override fun modifierValue(
        referenceCard: ScenarioReferenceCard?,
        variableValues: Map<String, Int>,
    ): Int? {
        return referenceCard?.tokens?.single { it.token == this }?.modifierValue?.let {
            when(it) {
                is ChaosTokenModifierValue.Static -> it.value
                is ChaosTokenModifierValue.Variable -> variableValues[it.key]?.times(-1)
            }
        }
    }
}

object AutoFail : ChaosToken {
    override fun toString(): String = "Auto-Fail"

    override fun modifierValue(
        referenceCard: ScenarioReferenceCard?,
        variableValues: Map<String, Int>,
    ): Int? {
        return Int.MIN_VALUE
    }
}

object ElderSign : ChaosToken {
    override fun toString(): String = "Elder Sign"

    override fun modifierValue(
        referenceCard: ScenarioReferenceCard?,
        variableValues: Map<String, Int>,
    ): Int? {
        return 0
    }
}

object Bless : ChaosToken {
    override fun toString(): String = "Bless"

    override fun modifierValue(
        referenceCard: ScenarioReferenceCard?,
        variableValues: Map<String, Int>,
    ): Int? {
        return 0
    }
}

object Curse : ChaosToken {
    override fun toString(): String = "Curse"

    override fun modifierValue(
        referenceCard: ScenarioReferenceCard?,
        variableValues: Map<String, Int>,
    ): Int? {
        return 0
    }
}

object Frost : ChaosToken {
    override fun toString(): String = "Frost"

    override fun modifierValue(
        referenceCard: ScenarioReferenceCard?,
        variableValues: Map<String, Int>,
    ): Int? {
        return 0
    }
}

@Serializable(with = ScenarioDifficultySerializer::class)
enum class ScenarioDifficulty(val value: String) {
    Easy("easy"), Standard("standard"), Hard("hard"), Expert("expert")
}

@Serializable
data class ScenarioChaosBag(
    val difficulty: ScenarioDifficulty,
    val tokens: List<ChaosToken>,
)

@Serializable
data class ScenarioReferenceCard(
    val difficulties: Set<ScenarioDifficulty>,
    val tokens: List<ScenarioReferenceChaosTokenValue>,
)

@Serializable(with = ChaosTokenModifierValueSerializer::class)
sealed interface ChaosTokenModifierValue {
    data class Static(
        val value: Int
    ) : ChaosTokenModifierValue

    data class Variable(
        val key: String
    ) : ChaosTokenModifierValue
}

@Serializable
data class ScenarioReferenceChaosTokenValue(
    val token: ChaosToken,
    val text: String,
    val modifierValue: ChaosTokenModifierValue,
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


object ScenarioDifficultySerializer : KSerializer<ScenarioDifficulty> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ChaosBagDifficulty", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ScenarioDifficulty) {
        encoder.encodeString(value.value)
    }

    override fun deserialize(decoder: Decoder): ScenarioDifficulty {
        val stringValue = decoder.decodeString()
        return ScenarioDifficulty
            .values()
            .singleOrNull { it.value == stringValue }
            ?: error("${stringValue} is not a valid Chaos Bag difficulty")
    }
}


object ChaosTokenModifierValueSerializer : KSerializer<ChaosTokenModifierValue> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("ChaosTokenModifierValue", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ChaosTokenModifierValue) {
        when (value) {
            is ChaosTokenModifierValue.Static -> {
                encoder.encodeString(value.value.toString())
            }

            is ChaosTokenModifierValue.Variable -> {
                encoder.encodeString(value.key)
            }
        }
    }

    override fun deserialize(decoder: Decoder): ChaosTokenModifierValue {
        val stringValue = decoder.decodeString()
        return stringValue
            .toIntOrNull()
            ?.let { ChaosTokenModifierValue.Static(it) }
            ?: ChaosTokenModifierValue.Variable(stringValue)
    }
}
