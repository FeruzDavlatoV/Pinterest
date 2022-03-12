package com.example.pinterest.model.profile

data class User(
    val id: String,
    val username: String,
    val name: String,
    var links: Links,
    var profileImage: ProfileImage,
    val followers_count: Long,
    val following_count: Long,
)
