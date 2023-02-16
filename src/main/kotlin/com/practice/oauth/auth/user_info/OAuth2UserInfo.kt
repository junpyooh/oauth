package com.practice.oauth.auth.user_info

import com.practice.oauth.domain.user.User

interface OAuth2UserInfo {
    fun getProviderId(): String
    fun getProvider(): String
    fun getEmail(): String
    fun getName(): String
    fun toEntity(): User
}