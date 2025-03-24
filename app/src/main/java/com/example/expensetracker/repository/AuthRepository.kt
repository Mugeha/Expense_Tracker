package com.example.expensetracker.repository

import android.content.Context
import android.util.Log
import com.example.expensetracker.data.remote.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException

class AuthRepository(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    // ✅ Login Function
    suspend fun login(email: String, password: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val response: LoginResponse = RetrofitClient.instance.login(LoginRequest(email, password))
                Log.d("AuthRepository", "✅ Login Successful: ${response.token}")

                response.token?.let {
                    saveAuthTokens(response.token, response.refreshToken, response.expiresIn)
                    Result.success(it)
                } ?: Result.failure(Exception("Invalid login response"))
            } catch (e: HttpException) {
                val errorMessage = parseErrorMessage(e)
                Log.e("AuthRepository", "❌ Login failed: $errorMessage")
                Result.failure(Exception(errorMessage))
            } catch (e: Exception) {
                Log.e("AuthRepository", "❌ Network error: ${e.message}")
                Result.failure(Exception("Network error. Check your internet connection."))
            }
        }
    }

    // ✅ Signup Function
    suspend fun signUp(email: String, username: String, password: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("AuthRepository", "🚀 Starting Signup Request")
                val response: SignupResponse = RetrofitClient.instance.register(SignupRequest(email, username, password))
                Log.d("AuthRepository", "✅ Signup Response: $response")

                if (!response.token.isNullOrEmpty() && !response.refreshToken.isNullOrEmpty()) {
                    saveAuthTokens(response.token, response.refreshToken, response.expiresIn)
                    Log.d("AuthRepository", "🔐 Tokens Saved Successfully")
                }

                return@withContext Result.success(response.message) // Continue even if token is null


                Log.e("AuthRepository", "❌ Invalid Signup Response - Missing Token")
                Result.failure(Exception("Invalid signup response"))
            } catch (e: HttpException) {
                val errorMessage = parseErrorMessage(e)
                Log.e("AuthRepository", "❌ Signup HttpException: $errorMessage")
                Result.failure(Exception(errorMessage))
            } catch (e: Exception) {
                Log.e("AuthRepository", "❌ Signup Exception: ${e.message}")
                Result.failure(Exception("Network error. Check your internet connection."))
            }
        }
    }


    fun saveAuthTokens(accessToken: String, refreshToken: String, expiresIn: Long) {
        val editor = sharedPreferences.edit()

        // Ensure minimum expiry of 1 hour (3600 seconds) if expiresIn is too short
        val safeExpiresIn = if (expiresIn < 3600) 3600 else expiresIn

        // Store expiry time in milliseconds
        val expiryTime = System.currentTimeMillis() + (safeExpiresIn * 1000)

        editor.putString("auth_token", accessToken)
        editor.putString("refresh_token", refreshToken)
        editor.putLong("token_expiry", expiryTime)
        editor.apply()

        Log.d("AuthRepository", "✅ Tokens saved: $accessToken, Safe Expiry (ms): $expiryTime, expiresIn (s): $safeExpiresIn")
    }



    suspend fun getValidToken(): String? {
        val accessToken = sharedPreferences.getString("auth_token", null)
        val refreshToken = sharedPreferences.getString("refresh_token", null)
        val expiryTime = sharedPreferences.getLong("token_expiry", 0)

        val currentTime = System.currentTimeMillis()

        // Extend buffer to 10 seconds for clock drift
        val bufferTime = 10000L

        val timeDiff = expiryTime - currentTime
        Log.d("AuthRepository", "🕒 Token Check - Current: $currentTime, Expiry: $expiryTime, Diff: $timeDiff ms")

        return when {
            accessToken == null || refreshToken == null -> {
                Log.e("AuthRepository", "❌ Missing token(s)")
                null
            }
            timeDiff > bufferTime -> accessToken // ✅ Valid token
            else -> {
                Log.w("AuthRepository", "🔄 Token expired - Attempting refresh")
                refreshAccessToken(refreshToken)
            }
        }
    }




    // ✅ Fetch Username
    suspend fun fetchUsername(): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val token = getValidToken() ?: return@withContext Result.failure(Exception("No valid token found"))
                val authHeader = "Bearer $token"
                val response = RetrofitClient.instance.autoLogin(authHeader)

                response.username.let {
                    Log.d("AuthRepository", "👤 Fetched Username: $it")
                    Result.success(it)
                }
            } catch (e: HttpException) {
                val errorMessage = parseErrorMessage(e)
                Log.e("AuthRepository", "❌ Fetch username failed: $errorMessage")
                Result.failure(Exception(errorMessage))
            } catch (e: Exception) {
                Log.e("AuthRepository", "❌ Network error: ${e.message}")
                Result.failure(Exception("Network error. Check your internet connection."))
            }
        }
    }


    // ✅ Fetch Email
    suspend fun fetchEmail(): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val token = getValidToken() ?: return@withContext Result.failure(Exception("No valid token found"))
                val authHeader = "Bearer $token"
                val response = RetrofitClient.instance.autoLogin(authHeader)

                response.email.let {
                    Log.d("AuthRepository", "📧 Fetched Email: $it")
                    Result.success(it)
                }
            } catch (e: HttpException) {
                val errorMessage = parseErrorMessage(e)
                Log.e("AuthRepository", "❌ Fetch email failed: $errorMessage")
                Result.failure(Exception(errorMessage))
            } catch (e: Exception) {
                Log.e("AuthRepository", "❌ Network error: ${e.message}")
                Result.failure(Exception("Network error. Check your internet connection."))
            }
        }
    }




    // ✅ Refresh Access Token Using Refresh Token
    private suspend fun refreshAccessToken(refreshToken: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.instance.refreshToken(RefreshTokenRequest(refreshToken))
                response.token?.let {
                    saveAuthTokens(it, response.refreshToken, response.expiresIn)
                    Log.d("AuthRepository", "🔄 Token refreshed successfully")
                    it
                }
            } catch (e: Exception) {
                Log.e("AuthRepository", "❌ Token refresh failed: ${e.message}")
                logout() // Clears tokens if refresh fails
                null
            }
        }
    }

    // ✅ Parse Error Message
    private fun parseErrorMessage(e: HttpException): String {
        val errorBody = e.response()?.errorBody()?.string()
        return JSONObject(errorBody ?: "{}").optString("message", "An error occurred. Try again.")
    }

    // ✅ Logout Function (Clear Tokens)
    fun logout() {
        sharedPreferences.edit().clear().apply() // Clears all user data
        Log.d("AuthRepository", "🚪 User logged out, preferences cleared")
    }
}
