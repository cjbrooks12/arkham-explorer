package com.caseyjbrooks.arkham.ui.pages

import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerRepository
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import kotlinx.coroutines.flow.map

class StaticPageInputHandler(
    private val repository: ArkhamExplorerRepository,
) : InputHandler<
    StaticPageContract.Inputs,
    StaticPageContract.Events,
    StaticPageContract.State,
    > {
    override suspend fun InputHandlerScope<StaticPageContract.Inputs, StaticPageContract.Events, StaticPageContract.State>.handleInput(
        input: StaticPageContract.Inputs
    ) = when (input) {
        is StaticPageContract.Inputs.Initialize -> {
            updateState { it.copy(slug = input.slug) }
            observeFlows(
                "Static Page Expansions",
                repository
                    .getExpansions(false)
                    .map { cached -> StaticPageContract.Inputs.ExpansionsLoaded(cached) }
            )
            observeFlows(
                "Static Page Content",
                repository
                    .getStaticPageContent(false, input.slug)
                    .map { cached -> StaticPageContract.Inputs.StaticPageContentLoaded(cached) }
            )
        }

        is StaticPageContract.Inputs.ExpansionsLoaded -> {
            updateState { it.copy(layout = MainLayoutState.fromCached(input.expansions)) }
        }

        is StaticPageContract.Inputs.StaticPageContentLoaded -> {
            updateState { it.copy(content = input.content) }
        }
    }
}
