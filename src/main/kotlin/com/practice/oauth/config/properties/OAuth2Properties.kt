package com.practice.oauth.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.google")
data class OAuth2Properties(
    val clientId: String,
    val clientSecret: String,
    val scope: List<String>,
    val redirectUri: String,
)