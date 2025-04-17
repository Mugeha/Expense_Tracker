package com.example.expensetracker.viewModel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.api.ApiService
import com.example.expensetracker.data.remote.SessionManager
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class PhotoViewModel(
    private val apiService: ApiService,
    private val sessionManager: SessionManager,
    private val context: Context
) : ViewModel() {

    private val _profileImageUri = MutableLiveData<Uri?>()
    val profileImageUri: LiveData<Uri?> get() = _profileImageUri

    init {
        loadProfileImage()
    }

    fun loadProfileImage() {
        val uriString = sessionManager.getProfileImage()
        val parsedUri = if (!uriString.isNullOrBlank() && uriString != "null") Uri.parse(uriString) else null
        Log.d("PhotoViewModel", "Loaded profile image URI: $parsedUri")
        _profileImageUri.postValue(parsedUri)
    }

    fun saveProfileImage(uri: Uri) {
        Log.d("PhotoViewModel", "Saving profile image URI: $uri")
        sessionManager.saveProfileImage(uri.toString())
        _profileImageUri.postValue(uri)
    }

    fun createImageUri(): Uri? {
        val file = File(context.cacheDir, "profile_${System.currentTimeMillis()}.jpg")
        return try {
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
            Log.d("PhotoViewModel", "Created image URI: $uri")
            uri
        } catch (e: Exception) {
            Log.e("PhotoViewModel", "Error creating image URI", e)
            null
        }
    }

    fun bitmapToUri(bitmap: Bitmap): Uri? {
        val file = File(context.cacheDir, "profile_image_${System.currentTimeMillis()}.png")
        return try {
            FileOutputStream(file).use { fos ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                fos.flush()
            }
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
            Log.d("PhotoViewModel", "Converted bitmap to URI: $uri")
            uri
        } catch (e: IOException) {
            Log.e("PhotoViewModel", "Error converting bitmap to URI", e)
            null
        }
    }

    fun uploadProfileImage(
        email: String,
        uri: Uri,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        Log.d("PhotoViewModel", "Starting upload for email: $email, URI: $uri")

        val file = File(context.cacheDir, "upload_profile.jpg")

        try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            Log.d("PhotoViewModel", "Image file saved locally for upload: ${file.absolutePath}")
        } catch (e: IOException) {
            val errorMsg = "Error processing image: ${e.localizedMessage}"
            Log.e("PhotoViewModel", errorMsg, e)
            onError(errorMsg)
            return
        }

        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val imagePart = MultipartBody.Part.createFormData("profileImage", file.name, requestFile)
        val emailPart = RequestBody.create("text/plain".toMediaTypeOrNull(), email)

        viewModelScope.launch {
            try {
                val response = apiService.uploadImage(emailPart, imagePart)
                if (response.isSuccessful) {
                    val uploadedImageUrl = "http://192.168.100.63:8080/uploads/${file.name}"
                    Log.d("PhotoViewModel", "Upload successful. Image URL: $uploadedImageUrl")

                    saveProfileImage(Uri.parse(uploadedImageUrl))
                    onSuccess()
                } else {
                    val errorMsg = "Failed to upload image: ${response.code()}"
                    Log.w("PhotoViewModel", errorMsg)
                    onError(errorMsg)
                }
            } catch (e: Exception) {
                val errorMsg = "Network error: ${e.localizedMessage}"
                Log.e("PhotoViewModel", errorMsg, e)
                onError(errorMsg)
            }
        }
    }
}
