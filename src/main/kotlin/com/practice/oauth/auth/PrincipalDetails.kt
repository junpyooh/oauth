package com.practice.oauth.auth

import com.practice.oauth.domain.user.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User


data class PrincipalDetails(
    private val user: User,
    private val attributes: MutableMap<String, Any>,
) : OAuth2User {
    override fun getName() = user.username

    override fun getAttributes() = attributes

    override fun getAuthorities(): MutableCollection<GrantedAuthority> {
        val collection: MutableCollection<GrantedAuthority> = mutableListOf()
        collection.add(GrantedAuthority { user.authorities.toString() })

        return collection
    }
}
