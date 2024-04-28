package edu.miu.cs489.insightHub.data

data class LoginResponse(
    val userId: Int,
    val token: String,
    val firstName: String,
    val lastName: String,
    val email: String
)

