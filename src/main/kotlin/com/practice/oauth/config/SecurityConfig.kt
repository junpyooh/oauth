package com.practice.oauth.config

import com.practice.oauth.auth.PrincipalOAuthUserService
import com.practice.oauth.config.properties.OAuth2Properties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository
import org.springframework.security.oauth2.client.web.AuthenticatedPrincipalOAuth2AuthorizedClientRepository
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val oAuth2Properties: OAuth2Properties,
    private val principalOAuthUserService: PrincipalOAuthUserService,
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .oauth2Login {
                it.authorizedClientRepository(
                    authorizedClientRepository(
                        authorizedClientService(
                            clientRegistrationRepository()
                        )
                    )
                )
                it.redirectionEndpoint().baseUri(googleClientRegistration().redirectUri)
                it.tokenEndpoint()
                it.userInfoEndpoint { userInfoConfig ->
                    userInfoConfig.userService(principalOAuthUserService)
                }
            }

        return http.build()
    }


    private fun authorizedClientService(
        clientRegistrationRepository: ClientRegistrationRepository?,
    ): OAuth2AuthorizedClientService {
        return InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository)
    }


    private fun authorizedClientRepository(
        authorizedClientService: OAuth2AuthorizedClientService?,
    ): OAuth2AuthorizedClientRepository {
        return AuthenticatedPrincipalOAuth2AuthorizedClientRepository(authorizedClientService)
    }

    private fun clientRegistrationRepository(): ClientRegistrationRepository {
        return InMemoryClientRegistrationRepository(googleClientRegistration())
    }

    private fun googleClientRegistration(): ClientRegistration {
        return ClientRegistration.withRegistrationId("google")
            .clientId(oAuth2Properties.clientId)
            .clientSecret(oAuth2Properties.clientSecret)
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUri("http://localhost:8080/login/oauth2/code/google")
            .scope("profile", "email")
            .authorizationUri("https://accounts.google.com/o/oauth2/auth")
            .tokenUri("https://oauth2.googleapis.com/token")
            .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
            .userNameAttributeName(IdTokenClaimNames.SUB)
            .jwkSetUri("https://www.googleapis.com/oauth2/v1/certs")
            .clientName("Google")
            .build()
    }
}