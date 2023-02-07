package com.practice.oauth.domain.user

import com.practice.oauth.domain.BaseEntity
import com.practice.oauth.internal.Authorities
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Entity
class User(
    @Column
    val name: String,
    @Column(unique = true)
    val token: String,
    @Column
    @Enumerated(EnumType.STRING)
    var authorities: Authorities = Authorities.USER,
) : BaseEntity() {

    fun updateAuthority(authorities: Authorities) {
        this.authorities = authorities
    }
}