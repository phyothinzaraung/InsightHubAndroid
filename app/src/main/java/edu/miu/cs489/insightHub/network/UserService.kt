package edu.miu.cs489.insightHub.network

import edu.miu.cs489.insightHub.data.LoginRequest
import edu.miu.cs489.insightHub.data.LoginResponse
import edu.miu.cs489.insightHub.data.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {

    @POST("user/register")
    suspend fun registerUser(@Body user: User): Response<User>

    @POST("user/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("user/{id}")
    suspend fun getUserById(@Path("id") userId: Int): Response<User>
}