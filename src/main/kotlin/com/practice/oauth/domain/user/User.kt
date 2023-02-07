package com.practice.oauth.domain.user

import com.practice.oauth.domain.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Entity
class User(
    @Column
    val name: String,
    @Column(unique = true)
    val email: String,
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var authorities: Role = Role.USER,
) : BaseEntity() {

    fun updateAuthority(role: Role) {
        this.authorities = role
    }
}