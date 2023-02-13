//package com.practice.oauth.auth.testService
//
//import com.fasterxml.jackson.databind.ObjectMapper
//import com.practice.oauth.config.properties.OAuth2Properties
//import org.springframework.http.ResponseEntity
//import org.springframework.stereotype.Component
//import org.springframework.web.client.RestTemplate
//import org.springframework.web.util.UriComponentsBuilder
//
//@Component
//class GoogleOAuth(
//    private val oAuth2Properties: OAuth2Properties,
//    private val objectMapper: ObjectMapper,
//    private val restTemplate: RestTemplate,
//) : SocialOAuth {
//    override fun getOAuthRedirectUrl(): String {
//        val url = AUTH_URL
//        val params = mutableMapOf<String, Any>()
//
//        params["scope"] = ""
//        params["response_type"] = "code"
//        params["client_id"] = oAuth2Properties.clientId
//        params["redirect_uri"] = oAuth2Properties.redirectUri
//
//        return url.toUrl(params)
//    }
//
//    override fun requestAccessToken(code: String): ResponseEntity<String> {
//        TODO("Not yet implemented")
//    }
//
//    override fun getAccessToken(accessToken: ResponseEntity<String>): GoogleOAuthToken {
//        TODO("Not yet implemented")
//    }
//
//    override fun requestUserInfo(googleOAuthToken: GoogleOAuthToken): ResponseEntity<String> {
//        TODO("Not yet implemented")
//    }
//
//    override fun getUserInfo(userInfoResponse: ResponseEntity<String>): GoogleUser {
//        TODO("Not yet implemented")
//    }
//
//    private fun String.toUrl(uriVariables: Map<String, Any>): String {
//        val builder = UriComponentsBuilder.fromHttpUrl(this)
//        uriVariables.forEach { builder.queryParam(it.key, it.value) }
//        return builder.encode().toUriString()
//    }
//
//    companion object{
//        const val AUTH_URL = "https://accounts.google.com/o/oauth2/auth"
//    }
//}