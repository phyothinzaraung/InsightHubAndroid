package edu.miu.cs489.insightHub.network

import edu.miu.cs489.insightHub.data.Content
import edu.miu.cs489.insightHub.data.ContentDeleteResponse
import edu.miu.cs489.insightHub.data.ContentUpdate
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ContentService {
    @POST("content/register")
    suspend fun addContent(@Body content: Content): Response<Content>

    @GET("content/list")
    suspend fun getContents(): Response<List<Content>>

    @GET("content/{id}")
    suspend fun getContentById(@Path("id") contentId: Int): Response<Content>

    @DELETE("content/delete/{id}")
    suspend fun deleteContent(@Path("id") contentId: Int): Response<ContentDeleteResponse>

    @PUT("content/update")
    suspend fun updateContent(@Body content: ContentUpdate): Response<Content>

}