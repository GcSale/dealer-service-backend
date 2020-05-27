package com.gcsale.dealerbackend.utils

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.data.domain.Sort

internal class SortUtilsTest {

    @Test
    fun getSortFromString() {
        assertEquals(Sort.by(Sort.Order.desc("name")), SortUtils.getSortFromString("-name"))
        assertEquals(Sort.by(Sort.Order.asc("name")), SortUtils.getSortFromString("name"))
        assertEquals(Sort.by(Sort.Order.asc("id")), SortUtils.getSortFromString("id"))
        assertEquals(Sort.by(Sort.Order.desc("id")), SortUtils.getSortFromString("-id"))
        assertEquals(Sort.by(Sort.Order.desc("id")), SortUtils.getSortFromString(null))
        assertEquals(Sort.by(Sort.Order.asc("name")), SortUtils.getSortFromString(null, "name"))
    }
}