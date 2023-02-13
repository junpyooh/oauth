package com.practice.oauth.auth

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import javax.servlet.http.HttpServletRequest

class CustomAuthorizationRequestResolver(
    clientRegistrationRepository: ClientRegistrationRepository,
    authorizationRequestBaseUri: String,
) : OAuth2AuthorizationRequestResolver {
    private val defaultResolver =
        DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, authorizationRequestBaseUri)

    override fun resolve(request: HttpServletRequest?): OAuth2AuthorizationRequest? {
        val req = defaultResolver.resolve(request) ?: return null

        return customizeAuthorizationRequest(req)
    }

    override fun resolve(request: HttpServletRequest?, clientRegistrationId: String?): OAuth2AuthorizationRequest? {
        val req = defaultResolver.resolve(request, clientRegistrationId) ?: return null

        return customizeAuthorizationRequest(req)
    }

    private fun customizeAuthorizationRequest(req: OAuth2AuthorizationRequest): OAuth2AuthorizationRequest? {
        val extraParams = HashMap<String, Any>()
        extraParams.putAll(req.additionalParameters)

        extraParams["response_type"] = "code"

        return OAuth2AuthorizationRequest
            .from(req)
            .additionalParameters(extraParams)
            .build()
    }
}
