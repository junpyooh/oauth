package com.practice.oauth.web.dto.request

import com.practice.oauth.domain.user.Role

data class AuthorityRequest(
    val authority: Role,
)