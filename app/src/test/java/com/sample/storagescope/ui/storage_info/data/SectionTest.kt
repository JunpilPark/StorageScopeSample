package com.sample.storagescope.ui.storage_info.data

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.lang.StringBuilder

internal class SectionTest {

    @Test
    fun 섹션에_하나의_Paragraph가_있을_때_toString을_호출하면_올바른_형식으로_나온다() {
        // given
        val section = Section()
        val paragraph = Paragraph("예제", "내용")
        val expect = StringBuilder().appendLine("예제 : ").appendLine("내용")

        // when
        section.append(paragraph)

        // then
        assertThat(section.toString()).isEqualTo(expect.toString())
    }

    @Test
    fun 섹션에_두개의_Paragraph가_있을_때_toString을_호출하면_두_Paragraph_사이에_빈라인이_삽입된다() {
        // given
        val section = Section()
        val paragraph1 = Paragraph("예제1", "내용1")
        val paragraph2 = Paragraph("예제2", "내용2")

        val expect = StringBuilder()
            .appendLine("${paragraph1.title} : ")
            .appendLine(paragraph1.contents)
            .appendLine()
            .appendLine("${paragraph2.title} : ")
            .appendLine(paragraph2.contents)

        // when
        section.append(paragraph1).append(paragraph2)

        // then
        assertThat(section.toString()).isEqualTo(expect.toString())
    }

    @Test
    fun 섹션의_clear를_호출하면_내용이_Empty가_된다() {
        // given
        val section = Section()
        val paragraph1 = Paragraph("예제1", "내용1")
        section.append(paragraph1)

        // when
        section.clear()

        // then
        assertThat(section.toString()).isEqualTo("")
    }
}