package edu.miu.cs489.insightHub.repository

import android.util.Log
import edu.miu.cs489.insightHub.data.Content
import edu.miu.cs489.insightHub.data.ContentDeleteResponse
import edu.miu.cs489.insightHub.data.ContentUpdate
import edu.miu.cs489.insightHub.network.ContentService
import javax.inject.Inject

class ContentRepository @Inject constructor(private val contentService: ContentService) {
    suspend fun addContent(content: Content): Result<Content>{
        return try {
            val response = contentService.addContent(content = content)
            Log.d("addContent Response>>>>", response.body().toString())
            if(response.isSuccessful){
                Result.success(response.body()!!)
            }else{
                Result.failure(Exception("cannot add"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun getContents(): Result<List<Content>>{
        return try {
            val response = contentService.getContents()
            Log.d("getContents Response>>>>", response.body().toString())
            if(response.isSuccessful){
                Result.success(response.body()!!)
            }else{
                Result.failure(Exception("cannot get contents"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun getContentById(contentId: Int): Result<Content>{
        return try {
            val response = contentService.getContentById(contentId = contentId)
            Log.d("getContentById Response>>>>", response.body().toString())
            if (response.isSuccessful){
                Result.success(response.body()!!)
            }else{
                Result.failure(Exception("cannot get content by id"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }

    }

    suspend fun deleteContent(contentId: Int): Result<ContentDeleteResponse>{
        return try {
            val response = contentService.deleteContent(contentId = contentId)
            Log.d("deleteContent Response>>>>", response.body().toString())
            if (response.isSuccessful){
                Result.success(response.body()!!)
            }else{
                Result.failure(Exception("Failed to delete content: ${response.message()}"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun updateContent(content: ContentUpdate): Result<Content>{
        return try {
            val response = contentService.updateContent(content = content)
            Log.d("updateContent Response>>>>", response.body().toString())
            if (response.isSuccessful){
                Result.success(response.body()!!)
            }else{
                Result.failure(Exception("cannot update content"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}