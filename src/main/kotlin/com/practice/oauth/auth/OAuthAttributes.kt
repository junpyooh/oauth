package com.practice.oauth.auth

import com.practice.oauth.domain.user.Role
import com.practice.oauth.domain.user.User

class OAuthAttributes constructor(
    private val attributes: Map<String, Any>,
    private val nameAttributeKey: String,
    private val name: String,
    private val email: String,
) {
    companion object {
        fun of(
            registrationId: String?,
            userNameAttributeName: String,
            attributes: Map<String, Any>,
        ): OAuthAttributes {
            return ofGoogle(userNameAttributeName, attributes)
        }

        private fun ofGoogle(userNameAttributeName: String, attributes: Map<String, Any>) =
            OAuthAttributes(
                attributes = attributes,
                nameAttributeKey = userNameAttributeName,
                name = attributes["name"].toString(),
                email = attributes["email"].toString()
            )
    }

    fun toEntity(): User {
        return User(
            name = name,
            email = email,
            authorities = Role.USER
        )
    }
}
