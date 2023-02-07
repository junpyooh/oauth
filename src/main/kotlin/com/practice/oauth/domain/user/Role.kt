package com.practice.oauth.domain.user

enum class Role(key: String, title: String) {
    SUPER("ROLE_SUPER", "super"),
    ADMIN("ROLE_ADMIN", "admin"),
    USER("ROLE_USER", "user")
}