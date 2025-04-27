package com.example.expensetracker.api

import com.example.expensetracker.model.LoginRequest
import com.example.expensetracker.model.LoginResponse
import com.example.expensetracker.model.UserResponse
import com.example.expensetracker.data.remote.SessionManager
import com.example.expensetracker.util.Constants
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    // ✅ Signup (Step 1)
    @Multipart
    @POST("api/auth/signup")
    suspend fun signup(
        @Part("username") username: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody
    ): Response<UserResponse>

    // ✅ Upload Profile Image (Step 2)
    @Multipart
    @POST("api/auth/upload-image")
    suspend fun uploadImage(
        @Part profileImage: MultipartBody.Part
    ): Response<Unit>

    // ✅ Login (Step 3)
    @POST("api/auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    companion object {
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
                .baseUrl(Constants.BASE_URL) // 👈🏽 Use Constants here now
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}
