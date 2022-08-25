package com.caseyjbrooks.arkham.utils.form

import com.copperleaf.forms.core.ui.UiSchema
import com.copperleaf.json.schema.JsonSchema
import kotlinx.serialization.json.JsonElement

class FormDefinition(
    val schema: JsonSchema,
    val uiSchema: UiSchema,
    val defaultData: JsonElement,
)
