package com.practice.oauth.auth.configurer

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.web.servlet.oauth2.login.TokenEndpointDsl
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class OAuth2LoginSecurityConfig(
    @Autowired private val login: OAuth2LoginConfig,
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .oauth2Login {
                it.authorizedClientRepository(
                    login.authorizedClientRepository(
                        login.authorizedClientService(
                            login.clientRegistrationRepository()
                        )
                    )
                )
                    .loginPage("/login")
                    .authorizationEndpoint()
                    .baseUri("/login/auth")
                    .authorizationRequestRepository(HttpSessionOAuth2AuthorizationRequestRepository())
                    .authorizationRequestResolver(
                        DefaultOAuth2AuthorizationRequestResolver(
                            login.clientRegistrationRepository(),
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
}