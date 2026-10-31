package com.example

import com.example.data.LiveMangaSiteScraper
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun parseChapters_nestedEplisterWithHeaderDivAndBtnLinks_extractsAllChapters() {
        val html = """
            <div class="eplister" id="chapterlist">
                <div class="ephead">
                    <span>رقم الفصل</span>
                    <span>تاريخ النشر</span>
                </div>
                <div class="ts-chl-collapsible-content">
                    <ul>
                        <li>
                            <a href="https://olympustaff.com/series/murim-psychopath/3" class="btn chapter-btn">
                                <div class="chbox">
                                    <div class="eph-num">
                                        <span class="chapternum">الفصل 3</span>
                                        <span class="chapterdate">2026-10-30</span>
                                    </div>
                                </div>
                            </a>
                        </li>
                        <li>
                            <div class="chbox">
                                <div class="ch-icon"><span>NEW</span></div>
                                <div class="eph-num">
                                    <a href="https://olympustaff.com/series/murim-psychopath/2">
                                        <span class="chapternum">الفصل 2</span>
                                        <span class="chapterdate">2026-10-25</span>
                                    </a>
                                </div>
                            </div>
                        </li>
                        <li>
                            <a href="/series/murim-psychopath/1">الفصل 1</a>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="related-manga">
                <a href="https://olympustaff.com/series/other-work/99">الفصل 99</a>
            </div>
        """.trimIndent()

        val chapters = LiveMangaSiteScraper.parseChaptersFromHtmlFragment(
            html = html,
            workId = "murim-psychopath",
            pageUrl = "https://olympustaff.com/series/murim-psychopath"
        )

        assertEquals(3, chapters.size)
        assertEquals(listOf(1, 2, 3), chapters.map { it.number })
        assertEquals("https://olympustaff.com/series/murim-psychopath/3", chapters[2].chapterUrl)
    }

    @Test
    fun parseChapters_percentEncodedArabicChapterUrls_extractsCorrectly() {
        val html = """
            <div class="page-content-listing">
                <div class="header-inner"><span>الفصول</span></div>
                <ul class="main version-chap">
                    <li class="wp-manga-chapter">
                        <a href="https://lekmanga.net/manga/murim-psychopath/%D8%A7%D9%84%D9%81%D8%B5%D9%84-12/">
                            الفصل 12
                        </a>
                        <span class="chapter-release-date"><i>منذ يومين</i></span>
                    </li>
                    <li class="wp-manga-chapter">
                        <a href="https://lekmanga.net/manga/murim-psychopath/%D9%81%D8%B5%D9%84-11/">
                            الفصل 11
                        </a>
                    </li>
                </ul>
            </div>
        """.trimIndent()

        val chapters = LiveMangaSiteScraper.parseChaptersFromHtmlFragment(
            html = html,
            workId = "murim-psychopath",
            pageUrl = "https://lekmanga.net/manga/murim-psychopath/"
        )

        assertEquals(2, chapters.size)
        assertEquals(listOf(11, 12), chapters.map { it.number })
    }
}

