package com.practice.oauth.auth

interface OAuth2UserInfo {
    fun getProviderId(): String
    fun getProvider(): String
    fun getEmail(): String
    fun getName(): String
}