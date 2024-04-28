package edu.miu.cs489.insightHub.repository

import android.util.Log
import edu.miu.cs489.insightHub.data.LoginRequest
import edu.miu.cs489.insightHub.data.LoginResponse
import edu.miu.cs489.insightHub.data.User
import edu.miu.cs489.insightHub.network.UserService
import javax.inject.Inject
class UserRepository @Inject constructor(private val userService: UserService) {

    suspend fun registerUser(user: User): Result<User>{
        return try {
            val response = userService.registerUser(user = user)
            Log.d("registerUser Response>>>>", response.body().toString())
            if(response.isSuccessful){
                Result.success(response.body()!!)
            }else{
                Result.failure(Exception("cannot register user"))
            }
        } catch (e: Exception){
            Result.failure(e)
        }

    }

    suspend fun loginUser(loginRequest: LoginRequest): Result<LoginResponse>{
        return try {
            val response = userService.login(loginRequest = loginRequest)
            Log.d("loginUser Response>>>>", response.body().toString())
            if(response.isSuccessful){
                Result.success(response.body()!!)
            }else{
                Result.failure(Exception("invalid credentials"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun getUserById(userId: Int): Result<User>{
        return try {
            val response = userService.getUserById(userId)
            Log.d("getUserById Response>>>>", response.body().toString())
            if (response.isSuccessful){
                Result.success(response.body()!!)
            }else{
                Result.failure(Exception("user not found"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }

    }
}