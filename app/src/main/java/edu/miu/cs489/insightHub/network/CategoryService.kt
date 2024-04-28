package edu.miu.cs489.insightHub.network

import edu.miu.cs489.insightHub.data.Category
import retrofit2.Response
import retrofit2.http.GET

interface CategoryService {

    @GET("category/list")
    suspend fun getCategories(): Response<List<Category>>
}