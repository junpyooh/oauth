package com.practice.oauth.auth.user_info

import com.practice.oauth.domain.user.Role
import com.practice.oauth.domain.user.User

class KakaoUserInfo(
    private val attributes: Map<String, Any>,
) : OAuth2UserInfo {

    private val attributesAccount = attributes["kakao_account"] as Map<String, Any>

    override fun getProviderId() = attributes["id"].toString()

    override fun getProvider() = KAKAO

    override fun getEmail() = attributesAccount["email"].toString()

    override fun getName() = "${getProvider()}_${getProviderId()}"

    override fun toEntity() =
        User(
            username = getName(),
            email = getEmail(),
            authorities = Role.USER,
            provider = getProvider(),
            providerId = getProviderId()
        )

    companion object {
        const val KAKAO = "kakao"
    }
}