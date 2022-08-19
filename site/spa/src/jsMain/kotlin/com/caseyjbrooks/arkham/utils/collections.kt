package com.caseyjbrooks.arkham.utils

import androidx.compose.runtime.Composable
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSection
import com.caseyjbrooks.arkham.utils.theme.bulma.Column
import com.caseyjbrooks.arkham.utils.theme.bulma.Row

fun <T> Collection<T>.removeAllPreservingDuplicates(items: Collection<T>): List<T> {
    val inputList = this@removeAllPreservingDuplicates
    return buildList {
        val remainingConsumedTokens = items.toMutableList()
        inputList.forEach { currentToken ->
            // this token is in the remaining consumed tokens, so remove it
            if (currentToken in remainingConsumedTokens) {
                val currentTokenIndexInRemaining = remainingConsumedTokens.indexOfFirst { it == currentToken }
                remainingConsumedTokens.removeAt(currentTokenIndexInRemaining)
            } else {
                // otherwise, pass the token through to the remainingTokens list
                this += currentToken
            }
        }
    }
}

data class GridItem(
    val columnWidth: String? = "is-4",
    val content: @Composable () -> Unit,
)

@Composable
fun DynamicGrid(
    vararg items: GridItem
) {
    DynamicGrid(items.toList())
}

@Composable
fun DynamicGrid(
    items: Collection<GridItem>
) {
    val chunks = items.chunked(3)

    BulmaSection {
        chunks.forEach { items ->
            Row("features", "is-centered") {
                items.forEach { item ->
                    Column(item.columnWidth) {
                        item.content()
                    }
                }
            }
        }
    }
}
