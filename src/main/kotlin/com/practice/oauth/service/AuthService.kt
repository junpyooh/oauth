package com.practice.oauth.service

import com.practice.oauth.web.dto.response.AuthenticationResponse
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService {

    @Transactional
    fun signIn(authentication: OAuth2AuthenticationToken): AuthenticationResponse {
        return AuthenticationResponse(
            authentication.authorizedClientRegistrationId,
            authentication.name,
            authentication.authorities,
            authentication.details,
            authentication.principal,
            authentication.isAuthenticated
        )
    }
}