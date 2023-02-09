package com.practice.oauth.domain.user

import com.practice.oauth.domain.BaseEntity
import org.hibernate.annotations.Comment
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Entity
class User(
    @Column
    val username: String,
    @Column(unique = true)
    val email: String,
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var authorities: Role = Role.USER,
    @Column
    @Comment("oauth type")
    val provider: String,
    @Column
    @Comment("oauth key(id)")
    val providerId: String
) : BaseEntity() {

    fun updateAuthority(role: Role) {
        this.authorities = role
    }
}