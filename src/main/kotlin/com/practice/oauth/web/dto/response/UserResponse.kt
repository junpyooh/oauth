package com.practice.oauth.web.dto.response

import com.practice.oauth.internal.Authorities

data class UserResponse(
    val userId: Long,
    val authority: Authorities,
    val token: String,
)