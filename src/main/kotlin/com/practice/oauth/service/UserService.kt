package com.practice.oauth.service

import com.practice.oauth.domain.user.UserRepository
import com.practice.oauth.web.dto.request.AuthorityRequest
import com.practice.oauth.web.dto.response.AuthorityResponse
import com.practice.oauth.web.dto.response.UserResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
) {

    @Transactional
    fun assignAuthority(userId: Long, request: AuthorityRequest) =
        userRepository.getReferenceById(userId).apply {
            this.updateAuthority(request.authority)
            userRepository.save(this)
        }.run {
            AuthorityResponse(
                userId = this.id ?: throw NoSuchElementException(),
                authority = this.authorities
            )
        }

    @Transactional(readOnly = true)
    fun getInfo(userId: Long) =
        userRepository.getReferenceById(userId).run {
            UserResponse(
                userId = this.id ?: throw NoSuchElementException(),
                authority = this.authorities,
                token = this.email
            )
        }
}