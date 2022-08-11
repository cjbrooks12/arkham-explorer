package com.caseyjbrooks.arkham.utils

fun <T> Collection<T>.removeAllPreservingDuplicates(items: Collection<T>): List<T> {
    val inputList = this@removeAllPreservingDuplicates
    return buildList {
        val remainingConsumedTokens = items.toMutableList()
        inputList.forEach { currentToken ->
            // this token is in the remaining consumed tokens, so remove it
            if(currentToken in remainingConsumedTokens) {
                val currentTokenIndexInRemaining = remainingConsumedTokens.indexOfFirst { it == currentToken }
                remainingConsumedTokens.removeAt(currentTokenIndexInRemaining)
            } else {
                // otherwise, pass the token through to the remainingTokens list
                this += currentToken
            }
        }
    }
}
