package com.example.expensetracker.repository

import android.util.Log
import com.example.expensetracker.api.ApiService
import com.example.expensetracker.data.remote.SessionManager
import com.example.expensetracker.model.LoginRequest
import com.example.expensetracker.model.LoginResponse
import com.example.expensetracker.model.UserResponse
import com.example.expensetracker.util.Constants
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class AuthRepository(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {

    private fun String.toRequestBody() =
        RequestBody.create("text/plain".toMediaTypeOrNull(), this)

    suspend fun signup(
        username: String,
        email: String,
        password: String
    ): Result<UserResponse> {
        return try {
            val usernamePart = username.toRequestBody()
            val emailPart = email.toRequestBody()
            val passwordPart = password.toRequestBody()

            val response = apiService.signup(usernamePart, emailPart, passwordPart)

            if (response.isSuccessful) {
                response.body()?.let { userResponse ->
                    Result.success(userResponse)
                } ?: Result.failure(Exception("Empty response from server"))
            } else {
                val errorMsg = response.errorBody()?.string()
                Log.e("AuthRepository", "Signup error: ${response.code()} - $errorMsg")
                Result.failure(Exception("Signup failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Exception during signup: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun uploadProfilePhoto(imageFile: File): Result<String> {
        return try {
            val imagePart = MultipartBody.Part.createFormData(
                "profileImage",
                imageFile.name,
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), imageFile)
            )

            val response = apiService.uploadImage(imagePart)

            if (response.isSuccessful) {
                val rawPath = response.body()?.profileImage
                val fullImageUrl = if (!rawPath.isNullOrEmpty()) {
                    "${Constants.BASE_URL}$rawPath"
                } else null

                return if (!fullImageUrl.isNullOrEmpty()) {
                    sessionManager.saveProfileImage(fullImageUrl)
                    Result.success(fullImageUrl)
                } else {
                    Result.failure(Exception("Server did not return image URL"))
                }
            } else {
                val errorMsg = response.errorBody()?.string()
                Log.e("AuthRepository", "Image upload error: ${response.code()} - $errorMsg")
                Result.failure(Exception("Image upload failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Exception during image upload: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = apiService.login(LoginRequest(email, password))

            if (response.isSuccessful) {
                response.body()?.let {
                    sessionManager.saveUserSession(it.token, it.username, it.profileImage)
                    sessionManager.saveEmail(it.email)
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response from server"))
            } else {
                val errorMsg = response.errorBody()?.string()
                Log.e("AuthRepository", "Login error: ${response.code()} - $errorMsg")
                Result.failure(Exception("Login failed: ${response.message()}"))
            }

        } catch (e: Exception) {
            Log.e("AuthRepository", "Exception during login: ${e.message}")
            Result.failure(e)
        }
    }

    fun logout() {
        sessionManager.clearSession()
        Log.d("AuthRepository", "User logged out successfully")
    }

    fun isLoggedIn(): Boolean = sessionManager.isLoggedIn()

    fun getUserToken(): String? = sessionManager.getToken()

    fun getUsername(): String? = sessionManager.getUsername()

    fun getProfileImage(): String? = sessionManager.getProfileImage()
}
