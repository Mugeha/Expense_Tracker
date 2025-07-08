import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.request.ImageRequest
import coil.compose.AsyncImage
import com.example.expensetracker.FilledButton
import com.example.expensetracker.R
import com.example.expensetracker.api.ApiService
import com.example.expensetracker.data.remote.SessionManager
import com.example.expensetracker.viewModel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    photoViewModel: PhotoViewModel,
    authViewModel: AuthViewModel,
    context: Context
) {
    val appContext = LocalContext.current
    val sessionManager = remember { SessionManager(appContext) }

    val username = remember { sessionManager.getUsername() }
    val email = remember { sessionManager.getEmail() }
    val storedProfileImage = remember { sessionManager.getProfileImage() }

    val profileImageUri by photoViewModel.profileImageUri.observeAsState()
    val finalProfileImage = remember(profileImageUri, storedProfileImage) {
        profileImageUri ?: storedProfileImage?.takeIf { it.isNotBlank() }?.let { Uri.parse(it) }
    }

    var isLoggingOut by remember { mutableStateOf(false) }
    var isDarkMode by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()
    var showSheet by remember { mutableStateOf(false) }

    val uploadResult = authViewModel.uploadResult.collectAsState().value

    LaunchedEffect(uploadResult) {
        uploadResult?.onSuccess { newImageUrl ->
            Log.d("ProfileScreen", "New profile image uploaded: $newImageUrl")
            sessionManager.saveProfileImage(newImageUrl)
            photoViewModel.saveProfileImage(Uri.parse(newImageUrl)) // 🔁 Also update viewmodel
        }
    }

    // 🧹 Clear any residual image if profile screen is loaded fresh
    LaunchedEffect(Unit) {
        if (profileImageUri == null && storedProfileImage.isNullOrEmpty()) {
            photoViewModel.clearProfileImage()
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            try {
                val file = photoViewModel.uriToFile(it)
                authViewModel.uploadProfilePhoto(photoFile = file)
            } catch (e: Exception) {
                Log.e("ProfileScreen", "Gallery Upload Error: ${e.message}")
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let {
            val uri = photoViewModel.bitmapToUri(it)
            uri?.let { safeUri ->
                try {
                    val file = photoViewModel.uriToFile(safeUri)
                    authViewModel.uploadProfilePhoto(photoFile = file)
                } catch (e: Exception) {
                    Log.e("ProfileScreen", "Camera Upload Error: ${e.message}")
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 28.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.back_button),
                contentDescription = "Back",
                modifier = Modifier
                    .size(64.dp)
                    .padding(top = 26.dp)
                    .clickable { navController.popBackStack() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier.size(150.dp),
            contentAlignment = Alignment.Center
        ) {
            // ✅ If finalProfileImage is null, fallback to human_profile explicitly
            Log.d("IMAGE_URL", "Displaying image from: $finalProfileImage")

            if (finalProfileImage != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(finalProfileImage)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(id = R.drawable.human_profile),
                    error = painterResource(id = R.drawable.human_profile), // fallback on failure
                    contentDescription = "Profile Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .clickable { navController.navigate("account-page") }

                )

            } else {
                Image(
                    painter = painterResource(id = R.drawable.human_profile),
                    contentDescription = "Default Profile",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                )
            }

            Image(
                painter = painterResource(id = R.drawable.camera_pic_removebg_preview),
                contentDescription = "Edit Profile",
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = (-10).dp, y = (-12).dp)
                    .clickable {
                        photoViewModel.clearProfileImage() // ✅ Clear cached uri before editing
                        showSheet = true
                    }
                    .alpha(0.9f)
            )
        }

        Text(
            text = username ?: "Loading...",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        ProfileSection("Personal Info") {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.clickable {
                    navController.navigate("change-details-screen")
                }
            ) {
                Text(text = "Edit", color = Color.Black)
                Image(
                    painter = painterResource(R.drawable.edit_image_2),
                    contentDescription = "Edit Button",
                    modifier = Modifier.size(15.dp)
                )
            }
            ProfileItem(R.drawable.human_profile, "Name", username ?: "Loading...")
            ProfileItem(R.drawable.mail_2, "Email", email ?: "Loading...")
        }

        ProfileSection("Account Info") {
            ProfileItem(R.drawable.settings_black, "Security", "Change Password", onClick = {
                navController.navigate("change-password-details")
            })
        }

        ProfileSection("App Settings") {
            ProfileItem(
                R.drawable.settings_black,
                "Theme",
                "Change Theme",
                isSwitch = true,
                switchState = isDarkMode,
                onSwitchChange = { isDarkMode = it }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        FilledButton(
            title = if (isLoggingOut) "Logging out..." else "Log out",
            enabled = !isLoggingOut,
            onClick = {
                isLoggingOut = true
                sessionManager.clearSession()
                photoViewModel.clearProfileImage() // ✅ Clear cache on logout
                navController.navigate("login-screen") {
                    popUpTo("home-screen") { inclusive = true }
                }
            }
        )

        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false },
                sheetState = sheetState
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Change Profile Picture",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "📷 Take Photo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                photoViewModel.clearProfileImage()
                                cameraLauncher.launch(null)
                                showSheet = false
                            }
                            .padding(12.dp)
                    )
                    Text(
                        text = "🖼️ Choose from Gallery",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                photoViewModel.clearProfileImage()
                                galleryLauncher.launch("image/*")
                                showSheet = false
                            }
                            .padding(12.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileSection(title: String, content: @Composable () -> Unit) {

    Spacer(modifier = Modifier.height(8.dp))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF0F0F0), shape = RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Text(text = title, fontSize = 18.sp, color = Color.Black, fontWeight = FontWeight.Bold)
        content()
    }
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun ProfileItem(
    imageRes: Int,
    title: String,
    subtitle: String,
    isSwitch: Boolean = false,
    switchState: Boolean = false,
    onClick: () -> Unit = {},
    onSwitchChange: ((Boolean) -> Unit)? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = title,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 16.sp, color = Color.Black)
            Text(text = subtitle, fontSize = 14.sp, color = Color.Gray)
        }
        if (isSwitch) {
            Switch(checked = switchState, onCheckedChange = onSwitchChange)
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun ProfileScreenPreview() {
//    val navController = rememberNavController()
//    ProfileScreen(navController = navController)
//}
