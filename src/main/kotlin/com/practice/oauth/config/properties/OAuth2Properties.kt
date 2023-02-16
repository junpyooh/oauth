package com.practice.oauth.config.properties

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Configuration

@ConstructorBinding
@Configuration
data class OAuth2Properties(
    @Value("spring.security.oauth2.client.registration.google")
    val googleClientId: String,
    @Value("spring.security.oauth2.client.registration.google")
    val googleClientSecret: String,
    @Value("spring.security.oauth2.client.registration.google")
    val googleScopes: List<String>,

    @Value("spring.security.oauth2.client.registration.kakao")
    val kakaoClientId: String,
    @Value("spring.security.oauth2.client.registration.kakao")
    val kakaoClientSecret: String,
    @Value("spring.security.oauth2.client.registration.kakao")
    val kakaoScopes: List<String>,
)