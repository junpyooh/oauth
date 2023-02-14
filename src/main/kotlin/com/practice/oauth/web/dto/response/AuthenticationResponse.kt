package com.practice.oauth.web.dto.response

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User

data class AuthenticationResponse(
    val authorizedClientRegistrationId: String,
    val name: String,
    val authorities: Collection<GrantedAuthority>,
    val details: Any,
    val principal: OAuth2User,
)