package com.practice.oauth.internal

import org.springframework.data.repository.CrudRepository

fun <T, ID : Any> CrudRepository<T, ID>.findByIdOrThrow(id: ID): T =
    findById(id).orElseThrow { throw NoSuchElementException("No value present for id $id") }