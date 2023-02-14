package com.practice.oauth.web.api.v1

import com.practice.oauth.service.AuthService
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val authService: AuthService,
) {
    @GetMapping("/loginSuccess")
    fun getLoginInfo(
        authentication: OAuth2AuthenticationToken,
    ) =
        authService.signIn(authentication)
}