package com.example.expensetracker.repository

import android.content.Context
import android.util.Log
import com.example.expensetracker.data.remote.LoginRequest
import com.example.expensetracker.data.remote.LoginResponse
import com.example.expensetracker.data.remote.SignupRequest
import com.example.expensetracker.data.remote.SignupResponse
import com.example.expensetracker.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException

class AuthRepository (private val context: Context){

    suspend fun login(email: String, password: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val response: LoginResponse = RetrofitClient.instance.login(LoginRequest(email, password))
                Log.d("AuthRepository", "Login Successful: ${response.token}")

                response.token?.let {
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

    suspend fun signUp(email: String, username: String, password: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val response: SignupResponse = RetrofitClient.instance.register(SignupRequest(email, username, password))
                Log.d("AuthRepository", "Signup Successful: ${response.token}")

                response.token?.let {
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

    fun saveAuthToken(token: String, expiresIn: Long) {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("auth_token", token) // Store the token
        editor.putLong("token_expiry", System.currentTimeMillis() + expiresIn) // Save expiry timestamp
        editor.apply()
    }


    private fun parseErrorMessage(e: HttpException): String {
        val errorBody = e.response()?.errorBody()?.string()
        return JSONObject(errorBody ?: "{}").optString("message", "An error occurred. Try again.")
    }

    fun logout() {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply() // Clears all user data
        Log.d("AuthRepository", "User logged out, preferences cleared")
    }
}
