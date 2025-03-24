package com.example.expensetracker.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

// ✅ Signup Request Model
data class SignupRequest(
    val email: String,
    val username: String,
    val password: String
)

// ✅ Signup Response Model
data class SignupResponse(
    val message: String,
    val token: String?,
    val refreshToken: String?,
    val expiresIn: Long // Expiration time in seconds
)

// ✅ Login Request Model
data class LoginRequest(
    val email: String,
    val password: String
)

// ✅ Updated Login Response Model
data class LoginResponse(
    @SerializedName("accessToken") val token: String,
    val refreshToken: String,
    val expiresIn: Long
)


// ✅ Refresh Token Request Model
data class RefreshTokenRequest(
    val refreshToken: String
)

// ✅ Refresh Token Response Model
data class RefreshTokenResponse(
    val token: String,
    val refreshToken: String,
    val expiresIn: Long
)

// ✅ Auto-login Response Model
data class AutoLoginResponse(
    val message: String,
    val email: String,
    val username: String
)

// ✅ Retrofit API Interface
interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("api/users/register")
    suspend fun register(@Body request: SignupRequest): SignupResponse

    @POST("api/users/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("api/users/refresh-token")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): RefreshTokenResponse

    // ✅ New: Auto-login API Call (GET Request with Authorization header)
    @GET("api/users/auto-login")
    suspend fun autoLogin(@Header("Authorization") token: String): AutoLoginResponse
}


// Retrofit Instance
object RetrofitClient {
    private const val BASE_URL = "http://192.168.100.240:8080/" // Ensure your IP is correct

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
