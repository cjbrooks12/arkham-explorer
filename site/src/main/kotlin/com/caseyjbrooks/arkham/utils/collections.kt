package com.caseyjbrooks.arkham.utils

fun <T : Comparable<T>> List<T>.findDuplicates(): Set<T> {
    val seen: MutableSet<T> = mutableSetOf()
    val duplicates: MutableSet<T> = mutableSetOf()
    for (i in this) {
        if (!seen.add(i)) {
            duplicates.add(i)
        }
    }
    return duplicates
}
