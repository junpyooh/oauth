package com.practice.oauth.auth.user_info

import com.practice.oauth.domain.user.Role
import com.practice.oauth.domain.user.User

class GoogleUserInfo(
    private val attributes: Map<String, Any>,
) : OAuth2UserInfo {

    override fun getProviderId() = attributes["sub"].toString()

    override fun getProvider() = GOOGLE

    override fun getEmail() = attributes["email"].toString()

    override fun getName() = attributes["name"].toString()

    companion object {
        const val GOOGLE = "google"
    }

    fun toEntity() =
        User(
            username = "${getProvider()}_${getProviderId()}",
            email = getEmail(),
            authorities = Role.USER,
            provider = getProvider(),
            providerId = getProviderId()
        )
}