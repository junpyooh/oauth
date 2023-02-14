package com.practice.oauth.config

import com.practice.oauth.auth.CustomRequestEntityConverter
import com.practice.oauth.auth.CustomTokenResponseConverter
import com.practice.oauth.auth.PrincipalOAuthUserService
import com.practice.oauth.config.properties.OAuth2Properties
import com.practice.oauth.domain.user.Role
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.FormHttpMessageConverter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.client.RestTemplate

@EnableWebSecurity
@Configuration
class SecurityConfig(
    private val oAuthProperties: OAuth2Properties,
    private val principalOAuthUserService: PrincipalOAuthUserService,
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeRequests()
            .antMatchers("/v1/super/**").hasRole("SUPER")
            .antMatchers("/v1/admin/**").hasRole("ADMIN")
            .antMatchers("/v1/customer/**").hasRole("USER")
            .anyRequest().authenticated()
            .and()

            .oauth2Login()

            .authorizationEndpoint()
            .authorizationRequestRepository(HttpSessionOAuth2AuthorizationRequestRepository())
            .and()

            .userInfoEndpoint()
            .userService(principalOAuthUserService)
            .userAuthoritiesMapper(userAuthoritiesMapper())
            .and()

            .tokenEndpoint()
            .accessTokenResponseClient(accessTokenResponseClient())
            .and()
            .defaultSuccessUrl("/loginSuccess")
            .and()

            .oauth2Client()
            .authorizationCodeGrant()
            .authorizationRequestRepository(HttpSessionOAuth2AuthorizationRequestRepository())

        return http.build()
    }

    @Bean
    fun userAuthoritiesMapper(): GrantedAuthoritiesMapper =
        GrantedAuthoritiesMapper { authorities: Collection<GrantedAuthority> ->
            val mappedAuthorities = mutableSetOf<GrantedAuthority>()

            authorities.forEach { authority ->
                if (authority is OAuth2UserAuthority) {
                    val userAttributes = authority.attributes
                    userAttributes["SUPER"] = Role.SUPER
                    userAttributes["ADMIN"] = Role.ADMIN
                    userAttributes["USER"] = Role.USER
                }
            }
            authorities.map {
                mappedAuthorities.add(SimpleGrantedAuthority(it.authority))
            }
            mappedAuthorities
        }

    @Bean
    fun accessTokenResponseClient(): OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {
        val accessTokenResponseClient = DefaultAuthorizationCodeTokenResponseClient()
        accessTokenResponseClient.setRequestEntityConverter(CustomRequestEntityConverter())

        val tokenResponseHttpMessageConverter = OAuth2AccessTokenResponseHttpMessageConverter()
        tokenResponseHttpMessageConverter.setAccessTokenResponseConverter(CustomTokenResponseConverter())

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
            .clientId(oAuthProperties.clientId)
            .clientSecret(oAuthProperties.clientSecret)
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUri(oAuthProperties.redirectUri)
            .scope(oAuthProperties.scope)
            .authorizationUri("https://accounts.google.com/o/oauth2/auth")
            .tokenUri("https://oauth2.googleapis.com/token")
            .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
            .userNameAttributeName(IdTokenClaimNames.SUB)
            .jwkSetUri("https://www.googleapis.com/oauth2/v1/certs")
            .clientName("Google")
            .build()
    }
}