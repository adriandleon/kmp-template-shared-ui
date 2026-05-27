package com.example.project.common.util

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.next

class UrlTest :
    FunSpec({
        test("extractPageInt should return 0 when page query parameter is not present") {
            val url = Url("https://example.com")
            url.extractPageInt() shouldBeExactly 0
        }

        test("extractPageInt should return the page number when page query parameter is present") {
            val page = Arb.int(max = 9999).next()
            val url = Url("https://example.com?page=$page")
            url.extractPageInt() shouldBeExactly page
        }

        test("extractPageInt should return 0 when page query parameter is invalid") {
            val url = Url("https://example.com?page=invalid")
            url.extractPageInt() shouldBeExactly 0
        }
    })
