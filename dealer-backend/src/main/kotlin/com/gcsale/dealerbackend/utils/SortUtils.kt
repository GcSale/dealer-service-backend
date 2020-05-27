package com.gcsale.dealerbackend.utils

import org.springframework.data.domain.Sort

object SortUtils {
    fun getSortFromString(sort: String?, defaultValue: String = "-id"): Sort {
        val sortString = if (sort != null && sort.isNotEmpty()) sort else defaultValue
        val direction = if (sortString[0] == '-') Sort.Direction.DESC else Sort.Direction.ASC
        val property = sortString.removePrefix("-")
        return Sort.by(Sort.Order(direction, property))
    }
}