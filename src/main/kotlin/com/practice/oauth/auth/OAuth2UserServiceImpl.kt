//package com.practice.oauth.auth
//
//import com.practice.oauth.domain.user.Role
//import com.practice.oauth.domain.user.UserRepository
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
//import org.springframework.security.oauth2.core.user.OAuth2User
//import org.springframework.stereotype.Service
//import java.util.*
//
//@Service
//class OAuth2UserServiceImpl(
//    private val userRepository: UserRepository,
//    private val encoder: BCryptPasswordEncoder,
//) : DefaultOAuth2UserService() {
//
//    override fun loadUser(userRequest: OAuth2UserRequest?): OAuth2User {
//        val oAuth2User = super.loadUser(userRequest)
//
//        val provider = userRequest?.clientRegistration?.registrationId
//        val providerId = oAuth2User.attributes["sub"].toString()
//        val username = "${provider}_${providerId}"
//
//        val uuid = UUID.randomUUID().toString().subSequence(0,6)
//        val password = encoder.encode("패스워드$uuid")
//        val email = oAuth2User.attributes["email"]
//        val role = Role.USER
//
//        val byUsername = userRepository.findByName(username)
//
//    }
//
//}