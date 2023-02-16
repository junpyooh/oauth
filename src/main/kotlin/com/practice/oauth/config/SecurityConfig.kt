package com.practice.oauth.config

import com.practice.oauth.auth.CustomClientProvider
import com.practice.oauth.auth.CustomRequestEntityConverter
import com.practice.oauth.auth.CustomTokenResponseConverter
import com.practice.oauth.auth.PrincipalOAuthUserService
import com.practice.oauth.domain.user.Role
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties
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
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.client.RestTemplate


@EnableWebSecurity
@Configuration
class SecurityConfig(
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
    fun clientRegistrationRepository(
        oAuth2ClientProperties: OAuth2ClientProperties,
    ): ClientRegistrationRepository {
        val registrations =
            oAuth2ClientProperties.registration.keys
                .stream()
                .map { client ->
                    getRegistration(oAuth2ClientProperties, client)
                }.toList().toMutableList()

        return InMemoryClientRegistrationRepository(registrations.toList())
    }

    private fun getRegistration(
        oAuth2ClientProperties: OAuth2ClientProperties,
        registrationId: String,
    ): ClientRegistration? {
        if (registrationId == "google") {
            val registration = oAuth2ClientProperties.registration["google"]
            return CustomClientProvider.GOOGLE.getBuilder(registrationId)
                .clientId(registration?.clientId)
                .clientSecret(registration?.clientSecret)
                .scope(registration?.scope)
                .build()
        }

        if (registrationId == "kakao") {
            val registration = oAuth2ClientProperties.registration["kakao"]
            return CustomClientProvider.KAKAO.getBuilder(registrationId)
                .clientId(registration?.clientId)
                .clientSecret(registration?.clientSecret)
                .scope(registration?.scope)
                .build()
        }

        return null
    }
}