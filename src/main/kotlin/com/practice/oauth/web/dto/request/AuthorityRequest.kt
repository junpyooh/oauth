package com.practice.oauth.web.dto.request

import com.practice.oauth.internal.Authorities

data class AuthorityRequest(
    val authority: Authorities,
)