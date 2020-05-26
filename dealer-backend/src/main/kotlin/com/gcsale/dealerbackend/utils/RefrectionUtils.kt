package com.gcsale.dealerbackend.utils

import org.springframework.core.ParameterizedTypeReference

object RefrectionUtils {
    inline fun <reified T> typeReference() = object : ParameterizedTypeReference<T>() {}
}