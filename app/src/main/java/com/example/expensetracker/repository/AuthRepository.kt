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
                Log.d("AuthRepository", "Login Successful: ${response.token}")

                response.token?.let {
                    saveAuthTokens(response.token, response.refreshToken, response.expiresIn)
                    Result.success(it)
                } ?: Result.failure(Exception("Invalid login response"))
            } catch (e: HttpException) {
                val errorMessage = parseErrorMessage(e)
                Log.e("AuthRepository", "Login failed: $errorMessage")
                Result.failure(Exception(errorMessage))
            } catch (e: Exception) {
                Log.e("AuthRepository", "Network error: ${e.message}")
                Result.failure(Exception("Network error. Check your internet connection."))
            }
        }
    }

    // ✅ Signup Function
    suspend fun signUp(email: String, username: String, password: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val response: SignupResponse = RetrofitClient.instance.register(SignupRequest(email, username, password))
                Log.d("AuthRepository", "Signup Successful: ${response.token}")

                response.token?.let {
                    response.refreshToken?.let { it1 ->
                        saveAuthTokens(response.token,
                            it1, response.expiresIn)
                    }
                    Result.success(it)
                } ?: Result.failure(Exception("Invalid signup response"))
            } catch (e: HttpException) {
                val errorMessage = parseErrorMessage(e)
                Log.e("AuthRepository", "Signup failed: $errorMessage")
                Result.failure(Exception(errorMessage))
            } catch (e: Exception) {
                Log.e("AuthRepository", "Network error: ${e.message}")
                Result.failure(Exception("Network error. Check your internet connection."))
            }
        }
    }

    // ✅ Save Tokens and Expiry Time
    fun saveAuthTokens(accessToken: String, refreshToken: String, expiresIn: Long) {
        val editor = sharedPreferences.edit()
        editor.putString("auth_token", accessToken)
        editor.putString("refresh_token", refreshToken)
        editor.putLong("token_expiry", System.currentTimeMillis() + expiresIn * 1000) // Convert seconds to ms
        editor.apply()
    }

    // ✅ Check and Refresh Token if Needed
    suspend fun getValidToken(): String? {
        val accessToken = sharedPreferences.getString("auth_token", null)
        val refreshToken = sharedPreferences.getString("refresh_token", null)
        val expiryTime = sharedPreferences.getLong("token_expiry", 0)

        return when {
            accessToken == null || refreshToken == null -> null // No token found, force logout
            System.currentTimeMillis() < expiryTime -> accessToken // Token is still valid
            else -> refreshAccessToken(refreshToken) // Token expired, try to refresh
        }
    }

    // ✅ Refresh Access Token Using Refresh Token
    private suspend fun refreshAccessToken(refreshToken: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.instance.refreshToken(RefreshTokenRequest(refreshToken))
                response.token?.let {
                    saveAuthTokens(it, response.refreshToken, response.expiresIn)
                    Log.d("AuthRepository", "Token refreshed successfully")
                    it
                }
            } catch (e: Exception) {
                Log.e("AuthRepository", "Token refresh failed: ${e.message}")
                logout() // Clear tokens if refresh fails
                null
            }
        }
    }

    // ✅ Parse Error Message
    private fun parseErrorMessage(e: HttpException): String {
        val errorBody = e.response()?.errorBody()?.string()
        return JSONObject(errorBody ?: "{}").optString("message", "An error occurred. Try again.")
    }

    // ✅ Logout Function
    fun logout() {
        sharedPreferences.edit().clear().apply() // Clears all user data
        Log.d("AuthRepository", "User logged out, preferences cleared")
    }
}
