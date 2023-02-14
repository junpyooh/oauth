package com.practice.oauth.web.api.v1

import com.practice.oauth.web.dto.response.AuthenticationResponse
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController {
    @GetMapping("/loginSuccess")
    fun getLoginInfo(
        authentication: OAuth2AuthenticationToken,
    ): AuthenticationResponse {
        return AuthenticationResponse(
            authentication.authorizedClientRegistrationId,
            authentication.name,
            authentication.authorities,
            authentication.details,
            authentication.credentials,
            authentication.principal,
        )
    }
}