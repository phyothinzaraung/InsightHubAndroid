package edu.miu.cs489.insightHub.repository

import android.util.Log
import edu.miu.cs489.insightHub.data.Category
import edu.miu.cs489.insightHub.network.CategoryService
import javax.inject.Inject

class CategoryRepository @Inject constructor(private val categoryService: CategoryService) {
    suspend fun getCategories(): Result<List<Category>>{
        return try {
            val response = categoryService.getCategories()
            Log.d("Category Response>>>>", response.body().toString())
            if (response.isSuccessful){
                Result.success(response.body()!!)
            }else{
                Result.failure(Exception("cannot retrieve"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}