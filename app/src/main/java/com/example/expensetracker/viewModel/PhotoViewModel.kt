import android.app.Application
import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.io.File

class PhotoViewModel(
    private val app: Application,
    private val authRepository: AuthRepository
) : AndroidViewModel(app) { // Notice AndroidViewModel here for context access

    private val _profileImageUri = MutableLiveData<Uri?>()
    val profileImageUri: LiveData<Uri?> get() = _profileImageUri

    private val _uploadResult = MutableLiveData<Result<Unit>>()
    val uploadResult: LiveData<Result<Unit>> get() = _uploadResult

    private val sharedPrefs = app.getSharedPreferences("user_prefs", Application.MODE_PRIVATE)

    fun saveProfileImage(uri: Uri) {
        _profileImageUri.value = uri
        sharedPrefs.edit().putString("profile_image_uri", uri.toString()).apply()
    }

    fun loadProfileImage() {
        val uriString = sharedPrefs.getString("profile_image_uri", null)
        _profileImageUri.value = uriString?.let { Uri.parse(it) }
    }

    fun createImageUri(): Uri? {
        val resolver: ContentResolver = app.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "profile_${System.currentTimeMillis()}.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        }
        return resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }

    fun uploadProfileImage(email: String, uri: Uri, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val file = uriToFile(uri)
                val result = authRepository.uploadProfileImage(email, file)
                if (result.isSuccess) {
                    _uploadResult.postValue(Result.success(Unit))
                    onSuccess()
                } else {
                    _uploadResult.postValue(Result.failure(result.exceptionOrNull() ?: Exception("Upload failed")))
                    onError(result.exceptionOrNull()?.message ?: "Unknown upload error")
                }
            } catch (e: Exception) {
                _uploadResult.postValue(Result.failure(e))
                onError(e.message ?: "Unknown error")
            }
        }
    }

    private fun uriToFile(uri: Uri): File {
        val inputStream = app.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Unable to open input stream for URI")
        val tempFile = File.createTempFile("upload_", ".jpg", app.cacheDir)
        tempFile.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }
        return tempFile
    }
}
