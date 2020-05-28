package com.gcsale.dealerbackend.infrastructure.web.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("rest")
class RestConfigProperties {
    lateinit var corsOrigins: String

}