import android.app.Application
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.expensetracker.data.remote.SessionManager
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID

class PhotoViewModel(
    private val app: Application,
    private val context: Context
) : AndroidViewModel(app) {

    private val _profileImageUri = MutableLiveData<Uri?>()
    val profileImageUri: LiveData<Uri?> get() = _profileImageUri

    private val sharedPrefs = app.getSharedPreferences("user_prefs", Application.MODE_PRIVATE)
    private val sessionManager = SessionManager(context)

    fun saveProfileImage(uri: Uri) {
        _profileImageUri.value = uri
        sharedPrefs.edit().putString("profile_image_uri", uri.toString()).apply()
    }

    fun loadProfileImage() {
        val fromSession = sessionManager.getProfileImage()?.let { Uri.parse(it) }
        val fromPrefs = sharedPrefs.getString("profile_image_uri", null)?.let { Uri.parse(it) }
        _profileImageUri.value = fromSession ?: fromPrefs
    }

    fun clearProfileImage() {
        _profileImageUri.value = null
        sharedPrefs.edit().remove("profile_image_uri").apply()
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
        val wrapper = android.content.ContextWrapper(context)
        val file = File(wrapper.getDir("images", Context.MODE_PRIVATE), "${UUID.randomUUID()}.jpg")
        return try {
            FileOutputStream(file).use { stream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            }
            Uri.fromFile(file)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun uriToFile(uri: Uri): File {
        val inputStream = app.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Unable to open input stream for URI")
        val tempFile = File.createTempFile("upload_", ".jpg", app.cacheDir)

        inputStream.use { input ->
            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        if (!tempFile.exists() || tempFile.length() == 0L) {
            throw IllegalStateException("File copy failed or file is empty")
        }

        return tempFile
    }
}
