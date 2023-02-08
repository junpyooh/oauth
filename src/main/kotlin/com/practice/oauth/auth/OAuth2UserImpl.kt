//package com.practice.oauth.auth
//
//import com.practice.oauth.domain.user.User
//import org.springframework.security.core.GrantedAuthority
//import org.springframework.security.oauth2.core.user.OAuth2User
//
//class OAuth2UserImpl(
//    private val user: User,
//    private val attributes: MutableMap<String, Any>,
//
//    ) : OAuth2User {
//    override fun getName() = attributes["sub"].toString()
//
//
//    override fun getAttributes() = attributes
//
//
//    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
//        val collect: MutableCollection<GrantedAuthority>
//
//        collect.add(user.authorities.toString())
//    }
//}