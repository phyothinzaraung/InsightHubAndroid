package edu.miu.cs489.insightHub.di

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.miu.cs489.insightHub.data.local.preferences.PreferencesManager
import edu.miu.cs489.insightHub.network.CategoryService
import edu.miu.cs489.insightHub.network.ContentService
import edu.miu.cs489.insightHub.network.UserService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesOkHttpInterceptor(): HttpLoggingInterceptor{
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun providesHttpClient(loggingInterceptor: HttpLoggingInterceptor,
                           sharedPreferencesManager: PreferencesManager): OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor{ chain ->
                val request = chain.request().newBuilder()
                    .header("Authorization", "Bearer ${sharedPreferencesManager.getAccessToken()}")
                    .build()
                chain.proceed(request)
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun providesGsonConverterFactory(): GsonConverterFactory{
        return GsonConverterFactory.create(GsonBuilder().create())
    }

    @Provides
    @Singleton
    fun provideRetrofitInstance(gsonConverterFactory: GsonConverterFactory, client: OkHttpClient): Retrofit{
        return Retrofit.Builder()
            .baseUrl("http://10.200.18.124:8080/insightHubWeb/api/v1/")
            .addConverterFactory(gsonConverterFactory)
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun providesUserService(retrofit: Retrofit): UserService{
        return retrofit.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun providesCategoryService(retrofit: Retrofit): CategoryService{
        return retrofit.create(CategoryService::class.java)
    }

    @Provides
    @Singleton
    fun providesContentService(retrofit: Retrofit): ContentService{
        return retrofit.create(ContentService::class.java)
    }
}