package com.practice.oauth.domain

import javax.persistence.*

@MappedSuperclass
abstract class BaseEntity : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}