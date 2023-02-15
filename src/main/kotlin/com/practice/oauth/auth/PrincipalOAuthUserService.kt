package com.practice.oauth.auth

import com.practice.oauth.auth.user_info.GoogleUserInfo
import com.practice.oauth.domain.user.UserRepository
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class PrincipalOAuthUserService(
    private val userRepository: UserRepository,
) : DefaultOAuth2UserService() {

    override fun loadUser(userRequest: OAuth2UserRequest?): OAuth2User {
        val oAuth2User = super.loadUser(userRequest)
        val oAuth2UserInfo = GoogleUserInfo(oAuth2User.attributes)
        val username = oAuth2UserInfo.toEntity().username
        val user = userRepository.findByUsername(username)
            ?: userRepository.save(oAuth2UserInfo.toEntity())

        return PrincipalDetails(user, oAuth2User.attributes)
    }
}