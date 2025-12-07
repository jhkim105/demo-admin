package com.example.demo.model

data class User(
    val id: Long,
    var email: String,
    var name: String,
    var password: String,
    var roles: MutableSet<Role> = mutableSetOf()
)
