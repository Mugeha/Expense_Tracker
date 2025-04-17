import android.util.Log
import com.example.expensetracker.api.ApiService
import com.example.expensetracker.api.LoginRequest
import com.example.expensetracker.api.LoginResponse
import com.example.expensetracker.api.UserResponse
import com.example.expensetracker.data.remote.SessionManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class AuthRepository(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {

    // Helper function to convert String to RequestBody
    private fun String.toRequestBody() = RequestBody.create("text/plain".toMediaTypeOrNull(), this)

    // ✅ Signup Method (User + Profile Image Upload)
    suspend fun signup(username: String, email: String, password: String, imageFile: File): Result<UserResponse> {
        return try {
            val usernamePart = username.toRequestBody()
            val emailPart = email.toRequestBody()
            val passwordPart = password.toRequestBody()

            // Step 1: Call signup API
            val response = apiService.signup(usernamePart, emailPart, passwordPart)

            if (response.isSuccessful) {
                response.body()?.let { userResponse ->
                    // Step 2: Upload the image separately
                    val imageUploadResult = uploadProfileImage(email, imageFile)

                    if (imageUploadResult.isSuccess) {
                        Result.success(userResponse) // Return user details
                    } else {
                        Result.failure(Exception("Signup succeeded but image upload failed"))
                    }
                } ?: Result.failure(Exception("Empty response from server"))
            } else {
                Log.e("AuthRepo", "Signup error: ${response.code()} - ${response.errorBody()?.string()}")
                Result.failure(Exception("Signup failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepo", "Exception during signup: ${e.message}")
            Result.failure(e)
        }
    }


    suspend fun uploadProfileImage(email: String, imageFile: File): Result<Unit> {
        return try {
            val emailPart = email.toRequestBody()
            val imagePart = MultipartBody.Part.createFormData(
                "profileImage",
                imageFile.name,
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), imageFile)
            )

            val response = apiService.uploadImage(emailPart, imagePart)

            if (response.isSuccessful) {
                Result.success(Unit) // Successfully uploaded image
            } else {
                Log.e("AuthRepo", "Image upload error: ${response.code()} - ${response.errorBody()?.string()}")
                Result.failure(Exception("Image upload failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepo", "Exception during image upload: ${e.message}")
            Result.failure(e)
        }
    }




    // ✅ Login Method (with Session Saving)
    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = apiService.login(LoginRequest(email, password))

            if (response.isSuccessful) {
                response.body()?.let {
                    // Save user session on successful login
                    sessionManager.saveUserSession(it.token, it.username, it.profileImage)
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response from server"))
            } else {
                Log.e("AuthRepo", "Login error: ${response.code()} - ${response.errorBody()?.string()}")

                Result.failure(Exception("Login failed: ${response.message()}"))
            }

        } catch (e: Exception) {
            Log.e("AuthRepo", "Exception during login: ${e.message}")
            Result.failure(e)
        }
    }

    // ✅ Logout Method (Clears Session)
    fun logout() {
        sessionManager.clearSession()
        Log.d("AuthRepo", "User logged out successfully")
    }

    // ✅ Check if User is Logged In
    fun isLoggedIn(): Boolean = sessionManager.isLoggedIn()

    // ✅ Retrieve User Info (Token, Username, Profile Image)
    fun getUserToken(): String? = sessionManager.getToken()
    fun getUsername(): String? = sessionManager.getUsername()
    fun getProfileImage(): String? = sessionManager.getProfileImage()
}
