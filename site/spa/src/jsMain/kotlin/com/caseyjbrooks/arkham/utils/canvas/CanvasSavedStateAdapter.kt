package com.caseyjbrooks.arkham.utils.canvas

import com.copperleaf.ballast.savedstate.RestoreStateScope
import com.copperleaf.ballast.savedstate.SaveStateScope
import com.copperleaf.ballast.savedstate.SavedStateAdapter
import com.copperleaf.json.utils.parseJson
import com.copperleaf.json.utils.toJsonString
import kotlinx.browser.window
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import org.w3c.dom.get
import org.w3c.dom.set

class CanvasSavedStateAdapter(
    private val canvasId: String?,
) : SavedStateAdapter<
    CanvasContract.Inputs,
    CanvasContract.Events,
    CanvasContract.State,
    > {

    override suspend fun SaveStateScope<
        CanvasContract.Inputs,
        CanvasContract.Events,
        CanvasContract.State>.save() {
        this.saveAll {
            window.localStorage["formData[$canvasId]"] = it.formData.toJsonString(false)
        }
    }

    override suspend fun RestoreStateScope<
        CanvasContract.Inputs,
        CanvasContract.Events,
        CanvasContract.State>.restore(): CanvasContract.State {
        val formData: JsonElement = window.localStorage["formData[$canvasId]"]
            ?.parseJson() ?: JsonNull
        return CanvasContract.State(
            formData = formData
        )
    }

    override suspend fun onRestoreComplete(restoredState: CanvasContract.State): CanvasContract.Inputs {
        return CanvasContract.Inputs.Initialize
    }
}
