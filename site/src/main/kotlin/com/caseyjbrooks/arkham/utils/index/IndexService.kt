package com.caseyjbrooks.arkham.utils.index

import com.caseyjbrooks.arkham.utils.findDuplicates

class IndexService {
    private var _index: Index? = null

    var index: Index
        get() = _index ?: error("Indexing not completed")
        set(value) {
            _index = value.verify()
        }

    private fun Index.verify(): Index {
        val duplicates = this
            .entries
            .map { it.output.realOutput }
            .findDuplicates()
        check(duplicates.isEmpty()) {
            "Multiple entries are attempting to create the same file: $duplicates"
        }

        return this
    }
}
