package com.practice.oauth.auth

import com.practice.oauth.auth.user_info.GoogleUserInfo
import com.practice.oauth.auth.user_info.KakaoUserInfo
import com.practice.oauth.domain.user.User
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
        val provider = userRequest?.clientRegistration?.registrationId
        val oAuth2User = super.loadUser(userRequest)

        when (provider) {
            provider.equals("google").toString() ->
                saveGoogleUser(GoogleUserInfo(oAuth2User.attributes)).run {
                    return@run PrincipalDetails(this, oAuth2User.attributes)
                }

            provider.equals("kakao").toString() ->
                saveKakaoUser(KakaoUserInfo(oAuth2User.attributes)).run {
                    return@run PrincipalDetails(this, oAuth2User.attributes)
                }
        }
        return oAuth2User
    }

    private fun saveGoogleUser(oAuth2UserInfo: GoogleUserInfo): User {
        val username = oAuth2UserInfo.toEntity().username
        return userRepository.findByUsername(username)
            ?: userRepository.save(oAuth2UserInfo.toEntity())
    }

    private fun saveKakaoUser(oAuth2UserInfo: KakaoUserInfo): User {
        val username = oAuth2UserInfo.toEntity().username
        return userRepository.findByUsername(username)
            ?: userRepository.save(oAuth2UserInfo.toEntity())
    }
}