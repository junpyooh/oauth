//package com.practice.oauth.auth.testService
//
//import org.springframework.http.ResponseEntity
//
//interface SocialOAuth {
//
//    fun getOAuthRedirectUrl(): String
//    fun requestAccessToken(code: String): ResponseEntity<String>
//
//    fun getAccessToken(accessToken: ResponseEntity<String>): GoogleOAuthToken
//
//    fun requestUserInfo(googleOAuthToken: GoogleOAuthToken): ResponseEntity<String>
//
//    fun getUserInfo(userInfoResponse: ResponseEntity<String>): GoogleUser
//}