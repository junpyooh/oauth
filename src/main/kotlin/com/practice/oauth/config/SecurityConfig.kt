package com.practice.oauth.config

import com.practice.oauth.auth.CustomAuthorizationRequestResolver
import com.practice.oauth.auth.PrincipalOAuthUserService
import com.practice.oauth.config.properties.OAuth2Properties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.FormHttpMessageConverter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.core.endpoint.DefaultMapOAuth2AccessTokenResponseConverter
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.client.RestTemplate

//@EnableWebSecurity
//@Configuration
class SecurityConfig(
    private val oAuth2Properties: OAuth2Properties,
    private val principalOAuthUserService: PrincipalOAuthUserService,
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .oauth2Login {
                it.authorizationEndpoint { authEndPoint ->
                    authEndPoint.authorizationRequestResolver(
                        CustomAuthorizationRequestResolver(
                            clientRegistrationRepository(),
                            "https://accounts.google.com/o/oauth2/auth"
                        )
                    )
                    authEndPoint.authorizationRequestRepository(HttpSessionOAuth2AuthorizationRequestRepository())
                }
                it.redirectionEndpoint().baseUri(oAuth2Properties.redirectUri)
                it.tokenEndpoint().accessTokenResponseClient(accessTokenResponseClient())
                it.userInfoEndpoint { userInfoConfig ->
                    userInfoConfig.userService(principalOAuthUserService)
                }
            }

        return http.build()
    }

    @Bean
    fun accessTokenResponseClient(): OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {
        val accessTokenResponseClient = DefaultAuthorizationCodeTokenResponseClient()
        accessTokenResponseClient.setRequestEntityConverter(OAuth2AuthorizationCodeGrantRequestEntityConverter())

        val tokenResponseHttpMessageConverter = OAuth2AccessTokenResponseHttpMessageConverter()
        tokenResponseHttpMessageConverter.setAccessTokenResponseConverter(DefaultMapOAuth2AccessTokenResponseConverter())

        val restTemplate = RestTemplate(
            listOf(
                FormHttpMessageConverter(),
                tokenResponseHttpMessageConverter
            )
        )
        restTemplate.errorHandler = OAuth2ErrorResponseErrorHandler()
        accessTokenResponseClient.setRestOperations(restTemplate)

        return accessTokenResponseClient
    }

    @Bean
    fun clientRegistrationRepository(): ClientRegistrationRepository {
        return InMemoryClientRegistrationRepository(googleClientRegistration())
    }

    private fun googleClientRegistration(): ClientRegistration {
        return ClientRegistration.withRegistrationId("google")
            .registrationId("google")
            .clientId(oAuth2Properties.clientId)
            .clientSecret(oAuth2Properties.clientSecret)
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUri(oAuth2Properties.redirectUri)
            .scope(oAuth2Properties.scope)
            .authorizationUri("https://accounts.google.com/o/oauth2/auth")
            .tokenUri("https://oauth2.googleapis.com/token")
            .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
            .userNameAttributeName(IdTokenClaimNames.SUB)
            .jwkSetUri("https://www.googleapis.com/oauth2/v1/certs")
            .clientName("Google")
            .build()
    }

}