package com.practice.oauth.auth.configurer

import com.practice.oauth.config.properties.OAuth2Properties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.web.servlet.oauth2.login.TokenEndpointDsl
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.web.AuthenticatedPrincipalOAuth2AuthorizedClientRepository
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class OAuth2LoginSecurityConfig(
    private val oAuth2Properties: OAuth2Properties,
) {

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

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
                    .loginPage("/login")
                    .authorizationEndpoint()
                    .baseUri("/login/auth")
                    .authorizationRequestRepository(HttpSessionOAuth2AuthorizationRequestRepository())
                    .authorizationRequestResolver(
                        DefaultOAuth2AuthorizationRequestResolver(
                            clientRegistrationRepository(),
                            "/login/auth"
                        )
                    )
                it.redirectionEndpoint().baseUri("/login/auth/callback/*")
                it.tokenEndpoint().accessTokenResponseClient(TokenEndpointDsl().accessTokenResponseClient)
                it.userInfoEndpoint()
                    .userService(DefaultOAuth2UserService())
                    .oidcUserService(OidcUserService())
                // TODO .userAuthoritiesMapper()
            }
        return http.build()
    }


    @Bean
    fun clientRegistrationRepository(): ClientRegistrationRepository {
        return InMemoryClientRegistrationRepository(googleClientRegistration())
    }

    @Bean
    fun authorizedClientService(
        clientRegistrationRepository: ClientRegistrationRepository?,
    ): OAuth2AuthorizedClientService {
        return InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository)
    }

    @Bean
    fun authorizedClientRepository(
        authorizedClientService: OAuth2AuthorizedClientService?,
    ): OAuth2AuthorizedClientRepository {
        return AuthenticatedPrincipalOAuth2AuthorizedClientRepository(authorizedClientService)
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