package com.practice.oauth.auth

import org.springframework.core.convert.converter.Converter
import org.springframework.http.RequestEntity
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames
import org.springframework.util.MultiValueMap

class CustomRequestEntityConverter : Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<*>> {

    private val defaultConverter = OAuth2AuthorizationCodeGrantRequestEntityConverter()

    override fun convert(source: OAuth2AuthorizationCodeGrantRequest): RequestEntity<Any> {

        val entity = defaultConverter.convert(source)
        val params = entity?.body as (MultiValueMap<String, String>)

        val clientRegistration = source.clientRegistration
        val authorizationExchange = source.authorizationExchange
        val redirectUri = authorizationExchange.authorizationRequest.redirectUri
        val state = authorizationExchange.authorizationRequest.state

        params[OAuth2ParameterNames.GRANT_TYPE] = source.grantType.value
        params[OAuth2ParameterNames.CODE] = authorizationExchange.authorizationResponse.code
        params[OAuth2ParameterNames.REDIRECT_URI] = redirectUri
        params[OAuth2ParameterNames.CLIENT_ID] = clientRegistration.clientId
        params[OAuth2ParameterNames.CLIENT_SECRET] = clientRegistration.clientSecret
        params[OAuth2ParameterNames.STATE] = state

        return RequestEntity<Any>(params, entity.headers, entity.method, entity.url)
    }
}