package com.sample.storagescope.ui.storage_info.data

import java.lang.StringBuilder


class Section {
    private val section: StringBuilder = StringBuilder()

    fun append(paragraph: Paragraph): Section {
        if(section.isNotEmpty()) {
            section.appendLine()
        }
        section.appendLine("${paragraph.title} : ")
            .appendLine(paragraph.contents)
        return this
    }

    override fun toString(): String {
        return section.toString()
    }

    fun clear() {
        section.clear()
    }
}