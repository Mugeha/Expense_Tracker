package com.example.expensetracker.api

import com.example.expensetracker.data.remote.SessionManager
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import okhttp3.OkHttpClient

interface ApiService {

    // Signup (with image upload)
    @Multipart
    @POST("api/auth/signup")
    suspend fun signup(
        @Part("username") username: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody
    ): Response<UserResponse>


    // Upload profile image (after sign-up)
    @Multipart
    @POST("api/auth/upload-image")
    suspend fun uploadImage(
        @Part("email") email: RequestBody,
        @Part profileImage: MultipartBody.Part
    ): Response<Unit>

    // Login
    @POST("api/auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    companion object {
        private const val BASE_URL = "http://192.168.100.63:8080/"

        fun create(sessionManager: SessionManager): ApiService {
            val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val requestBuilder = chain.request().newBuilder()

                    // Attach token if available
                    sessionManager.getToken()?.let { token ->
                        requestBuilder.addHeader("Authorization", "Bearer $token")
                    }

                    chain.proceed(requestBuilder.build())
                }
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }

    }
}

// Data Models
data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val username: String,
    val profileImage: String
)

data class UserResponse(
    val id: Long,
    val username: String,
    val email: String,
    val profileImage: String,
    val token: String
)

