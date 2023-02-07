package com.practice.oauth.web.dto.response

import com.practice.oauth.domain.user.Role

data class AuthorityResponse(
    val userId: Long,
    val authority: Role,
)