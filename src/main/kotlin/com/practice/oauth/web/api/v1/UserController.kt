package com.practice.oauth.web.api.v1

import com.practice.oauth.service.UserService
import com.practice.oauth.web.dto.request.AuthorityRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/users")
class UserController(
    private val userService: UserService,
) {
    @GetMapping("{userId}")
    fun getInfo(@PathVariable userId: Long) =
        userService.getInfo(userId)

    @PatchMapping("{userId}")
    fun updateAuthority(
        @PathVariable userId: Long,
        @RequestBody request: AuthorityRequest,
    ) =
        userService.assignAuthority(userId, request)
}