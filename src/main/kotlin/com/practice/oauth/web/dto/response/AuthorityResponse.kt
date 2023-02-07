package com.practice.oauth.web.dto.response

import com.practice.oauth.internal.Authorities

data class AuthorityResponse(
    val userId: Long,
    val authority: Authorities,
)