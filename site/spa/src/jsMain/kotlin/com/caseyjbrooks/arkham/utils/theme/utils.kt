package com.caseyjbrooks.arkham.utils.theme

import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaColor
import com.copperleaf.arkham.models.api.ProductType
import com.copperleaf.arkham.models.api.ScenarioEncounterSet

val ProductType.color: BulmaColor
    get() = when (this) {
        ProductType.CoreSet -> BulmaColor.Link
        ProductType.DeluxeExpansion -> BulmaColor.Link
        ProductType.CampaignExpansion -> BulmaColor.Success
        ProductType.InvestigatorExpansion -> BulmaColor.Info
        ProductType.ReturnTo -> BulmaColor.Danger
        ProductType.MythosPack -> BulmaColor.Primary
        ProductType.ScenarioPack -> BulmaColor.Warning
        ProductType.InvestigatorStarterDeck -> BulmaColor.Warning
    }

val ScenarioEncounterSet.color: BulmaColor
    get() = if (this.conditional) {
        BulmaColor.Info
    } else if (this.setAside) {
        BulmaColor.Success
    } else if (this.partial) {
        BulmaColor.Danger
    } else {
        BulmaColor.Primary
    }

val ScenarioEncounterSet.tooltip: String
    get() = if (this.conditional) {
        "Conditional"
    } else if (this.setAside) {
        "Set Aside"
    } else if (this.partial) {
        "Partial"
    } else {
        "Full"
    }
