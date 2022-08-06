package com.caseyjbrooks.arkham.utils.resourceloader

interface ResourceLoader {
    suspend fun load(url: String): String
}
