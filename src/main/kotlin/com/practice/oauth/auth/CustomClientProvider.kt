package com.practice.oauth.auth

import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames

@Configuration
enum class CustomClientProvider {
    GOOGLE {
        override fun getBuilder(registrationId: String) =
            getBuilder(
                registrationId,
                ClientAuthenticationMethod.CLIENT_SECRET_BASIC,
                "$DEFAULT_REDIRECT_URL/$registrationId"
            ).apply {
                authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                authorizationUri("https://accounts.google.com/o/oauth2/auth")
                tokenUri("https://oauth2.googleapis.com/token")
                userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                userNameAttributeName(IdTokenClaimNames.SUB)
                jwkSetUri("https://www.googleapis.com/oauth2/v1/certs")
                clientName("Google")
            }
    },

    KAKAO {
        override fun getBuilder(registrationId: String) =
            getBuilder(
                registrationId,
                ClientAuthenticationMethod.CLIENT_SECRET_POST,
                "$DEFAULT_REDIRECT_URL/$registrationId"
            ).apply {
                authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .authorizationUri("https://kauth.kakao.com/oauth/authorize")
                    .tokenUri("https://kauth.kakao.com/oauth/token")
                    .userInfoUri("https://kapi.kakao.com/v2/user/me")
                    .userNameAttributeName("id")
                    .jwkSetUri("temp")
                    .clientName("Kakao")
            }
    };

    companion object {
        const val DEFAULT_REDIRECT_URL = "http://localhost:8080/login/oauth2/code"

        fun getBuilder(
            registrationId: String,
            method: ClientAuthenticationMethod,
            redirectUri: String,
        ): ClientRegistration.Builder =
            ClientRegistration.withRegistrationId(registrationId)
                .clientAuthenticationMethod(method)
                .redirectUri(redirectUri)
    }

    abstract fun getBuilder(registrationId: String): ClientRegistration.Builder
}
