package com.example.expensetracker.viewModel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.ByteArrayOutputStream
import java.io.File

class PhotoViewModel : ViewModel() {

    private val imageKey = "profile_image_uri"
    private val _profileImageUri = MutableLiveData<Uri?>()
    val profileImageUri: LiveData<Uri?> = _profileImageUri

    // ✅ Create a file URI for the camera
    fun createImageUri(context: Context): Uri {
        val file = File(context.filesDir, "profile_image.jpg")
        return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    }

    // ✅ Save Image URI and update LiveData
    fun saveProfileImage(context: Context, uri: Uri) {
        val sharedPref = context.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
        sharedPref.edit().putString(imageKey, uri.toString()).apply()
        _profileImageUri.postValue(uri) // Update LiveData
    }

    // ✅ Alias for saveProfileImage
    fun updateProfileImage(context: Context, uri: Uri) {
        saveProfileImage(context, uri)
    }

    // ✅ Convert Bitmap to Uri
    fun bitmapToUri(context: Context, bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            context.contentResolver,
            bitmap,
            "profile_image",
            null
        )
        return Uri.parse(path)
    }

    // ✅ Load Image URI from SharedPreferences
    fun loadProfileImage(context: Context) {
        val sharedPref = context.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
        val uriString = sharedPref.getString(imageKey, null)
        _profileImageUri.postValue(uriString?.let { Uri.parse(it) })
    }
}
