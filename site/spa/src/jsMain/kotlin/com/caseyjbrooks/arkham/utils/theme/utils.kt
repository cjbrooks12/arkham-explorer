package com.caseyjbrooks.arkham.utils.theme

import androidx.compose.runtime.Composable
import com.caseyjbrooks.arkham.app.BuildConfig
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaColor
import com.caseyjbrooks.arkham.utils.theme.bulma.Card
import com.copperleaf.arkham.models.api.InvestigatorClass
import com.copperleaf.arkham.models.api.InvestigatorLite
import com.copperleaf.arkham.models.api.ProductType
import com.copperleaf.arkham.models.api.ScenarioEncounterSet
import org.jetbrains.compose.web.attributes.ATarget
import org.jetbrains.compose.web.attributes.download
import org.jetbrains.compose.web.attributes.target
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.fontFamily
import org.jetbrains.compose.web.css.marginBottom
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

val ProductType.color: BulmaColor
    get() = when (this) {
        ProductType.CoreSet -> BulmaColor.Link
        ProductType.DeluxeExpansion -> BulmaColor.Link
        ProductType.CampaignExpansion -> BulmaColor.Success
        ProductType.InvestigatorExpansion -> BulmaColor.Info
        ProductType.ReturnTo -> BulmaColor.Danger
        ProductType.MythosPack -> BulmaColor.Primary
        ProductType.ScenarioPack -> BulmaColor.Warning
        ProductType.InvestigatorStarterDeck -> BulmaColor.Success
    }

val InvestigatorLite.color: BulmaColor
    get() = when (this.mainClass) {
        InvestigatorClass.Guardian -> BulmaColor.Info
        InvestigatorClass.Seeker -> BulmaColor.Warning
        InvestigatorClass.Rogue -> BulmaColor.Success
        InvestigatorClass.Mystic -> BulmaColor.Primary // TODO: make a purple color for this
        InvestigatorClass.Survivor -> BulmaColor.Danger
        InvestigatorClass.Neutral -> BulmaColor.Link
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

private data class CardIcon(
    val baseIconPath: String,
    val iconTitle: String,
    val extension: String,
    val size: Int?,
) {
    val url: String = if(size != null) {
        "${BuildConfig.BASE_URL}/assets/$size/$baseIconPath.$extension"
    } else {
        "${BuildConfig.BASE_URL}/assets/$baseIconPath.$extension"
    }
    val buttonText: String = if(size != null) {
        ".$extension (${size}px)"
    } else {
        ".$extension"
    }
    val downloadFilename: String = if(size != null) {
        "$iconTitle-${size}px.$extension"
    } else {
        "$iconTitle.$extension"
    }
}
@Composable
fun DownloadIconsCard(baseIconUrl: String) {
    val baseIconPath = baseIconUrl
        .removePrefix(BuildConfig.BASE_URL)
        .removePrefix("/assets/")
        .removeSuffix(".svg")
    val iconTitle = baseIconPath.split("/").last()

    val urls = listOf(
        CardIcon(baseIconPath, iconTitle, "svg", null),
        CardIcon(baseIconPath, iconTitle, "png", null),
        CardIcon(baseIconPath, iconTitle, "png", 48),
        CardIcon(baseIconPath, iconTitle, "png", 64),
        CardIcon(baseIconPath, iconTitle, "png", 128),
        CardIcon(baseIconPath, iconTitle, "png", 256),
        CardIcon(baseIconPath, iconTitle, "png", 512),
        CardIcon(baseIconPath, iconTitle, "png", 1024),
    )

    Card(
        title = "Icons",
        description = "Download this icon, availble in the following formats:"
    ) {
        urls.forEachIndexed { index, icon ->
            Div({
                if (index != urls.lastIndex) {
                    style { marginBottom(1.cssRem) }
                }
            }) {
                A(icon.url, {
                    classes("button", "is-link", "modal-button")
                    style { fontFamily("Teutonic") }
                    target(ATarget.Blank)
                    download(icon.downloadFilename)
                }) {
                    Text(icon.buttonText)
                }
            }
        }
    }
}
