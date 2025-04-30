import android.app.Application
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

class PhotoViewModel(
    private val app: Application,
    private val authRepository: AuthRepository,
    private val context: Context
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

    fun bitmapToUri(bitmap: Bitmap): Uri? {
        val wrapper = ContextWrapper(context)
        val file = File(wrapper.getDir("images", Context.MODE_PRIVATE), "${UUID.randomUUID()}.jpg")
        var stream: FileOutputStream? = null
        return try {
            stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            Uri.fromFile(file)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } finally {
            stream?.close()
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
