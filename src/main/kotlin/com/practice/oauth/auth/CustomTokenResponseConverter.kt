package com.practice.oauth.auth

import org.springframework.core.convert.converter.Converter
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames

class CustomTokenResponseConverter : Converter<Map<String, Any>, OAuth2AccessTokenResponse> {

    override fun convert(source: Map<String, Any>): OAuth2AccessTokenResponse? {
        val accessToken = source[OAuth2ParameterNames.ACCESS_TOKEN].toString()
        val refreshToken = source["id_token"].toString()

        val scopes = emptySet<String>()
        if (source.containsKey(OAuth2ParameterNames.SCOPE)) {
            val scope = source[OAuth2ParameterNames.SCOPE]
            scopes.plus(scope)
        }
        return OAuth2AccessTokenResponse.withToken(accessToken)
            .tokenType(TokenType.BEARER)
            .refreshToken(refreshToken)
            .scopes(scopes)
            .build()
    }
}