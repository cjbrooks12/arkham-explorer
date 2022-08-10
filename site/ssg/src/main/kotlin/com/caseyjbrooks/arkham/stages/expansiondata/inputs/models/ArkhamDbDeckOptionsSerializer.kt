package com.caseyjbrooks.arkham.stages.expansiondata.inputs.models

import com.caseyjbrooks.arkham.dag.http.prettyJson
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive

object ArkhamDbDeckOptionsSerializer : KSerializer<List<ArkhamDbCard.DeckOptions>> {
    private val realSerializer = ListSerializer(ArkhamDbCard.DeckOptions.serializer())
    override val descriptor: SerialDescriptor = realSerializer.descriptor

    override fun deserialize(decoder: Decoder): List<ArkhamDbCard.DeckOptions> {
        check(decoder is JsonDecoder)

        val element = decoder.decodeJsonElement()

        return if(element is JsonPrimitive && element.isString) {
            prettyJson.decodeFromString(realSerializer, element.content)
        } else if(element is JsonArray) {
            prettyJson.decodeFromJsonElement(realSerializer, element)
        } else {
            error("Unknown JSON type: $element")
        }
    }

    override fun serialize(encoder: Encoder, value: List<ArkhamDbCard.DeckOptions>) {
        encoder.encodeSerializableValue(realSerializer, value)
    }

}
