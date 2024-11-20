package com.trainAi.backend.dataUpload

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "my")
class TokenIssuer {
    lateinit var token: String
}
