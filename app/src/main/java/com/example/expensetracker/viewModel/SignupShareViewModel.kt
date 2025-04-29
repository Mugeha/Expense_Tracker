import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SignupSharedViewModel : ViewModel() {
    var username: String = ""
    var email: String = ""
    var password: String = ""

    fun saveUserData(username: String, email: String, password: String) {
        this.username = username
        this.email = email
        this.password = password
    }
}

