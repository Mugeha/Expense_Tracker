package com.example.expensetracker.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

// Signup Request Model
data class SignupRequest(
    val email: String,
    val username: String,
    val password: String
)

// Signup Response Model
data class SignupResponse(
    val message: String,
    val token: String? // ✅ Token is nullable to handle cases where it's not returned
)

// Login Request Model
data class LoginRequest(
    val email: String,
    val password: String
)

// Login Response Model
data class LoginResponse(
    val token: String
)

// Retrofit API Interface
interface ApiService {
    @Headers("Content-Type: application/json")

    @POST("api/users/register")
    suspend fun register(@Body request: SignupRequest): SignupResponse

    @POST("api/users/login") // ✅ Ensure this matches your backend's login endpoint
    suspend fun login(@Body request: LoginRequest): LoginResponse
}

// Retrofit Instance
object RetrofitClient {
    private const val BASE_URL = "http://192.168.100.240:8080/"  // Change to your actual IP if using a physical device

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
