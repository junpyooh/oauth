package com.practice.oauth.web.dto.response

import com.practice.oauth.domain.user.Role

data class UserResponse(
    val userId: Long,
    val authority: Role,
    val token: String,
)