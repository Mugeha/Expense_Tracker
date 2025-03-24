import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.expensetracker.FilledButton
import com.example.expensetracker.viewModel.ProfileViewModel
import com.example.expensetracker.R
import com.example.expensetracker.viewModel.PhotoViewModel
import com.example.expensetracker.viewModel.ProfileViewModelFactory
import com.example.expensetracker.viewModel.UsernameViewModel
import com.example.expensetracker.viewModel.UsernameViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    context: Context,
    photoViewModel: PhotoViewModel
) {
    val profileViewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(context))
    val usernameViewModel: UsernameViewModel = viewModel(factory = UsernameViewModelFactory(context))
    val username by usernameViewModel.username.collectAsState()

    val profileImageUri by photoViewModel.profileImageUri.observeAsState()

    var isLoggingOut by remember { mutableStateOf(false) }
    var isDarkMode by remember { mutableStateOf(false) }

    // State for Bottom Sheet
    val sheetState = rememberModalBottomSheetState()
    var showSheet by remember { mutableStateOf(false) }

    // Gallery launcher (no changes needed)
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { photoViewModel.updateProfileImage(context, it) }
    }

// Camera launcher: Convert bitmap to Uri
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let {
            val uri = photoViewModel.bitmapToUri(context, it)
            photoViewModel.updateProfileImage(context, uri)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 28.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Back Button
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

        // Profile Picture
        Box(
            modifier = Modifier.size(150.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = profileImageUri ?: R.drawable.human_profile,
                contentDescription = "Profile Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp) // Ensure the image fits the Box size
                    .clip(CircleShape)
                    .clickable { navController.navigate("account-page") }
            )

            Image(
                painter = painterResource(id = R.drawable.camera_pic_removebg_preview),
                contentDescription = "Edit Profile",
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = (-10).dp, y = (-12).dp)
                    .clickable { showSheet = true } // Open bottom sheet
                    .alpha(0.9f)
            )
        }

        // Profile Name
        Text(
            text = username ?: "Loading...",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Sections
        ProfileSection("Personal Info") {
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(text = "Edit", color = Color.Black)
                Image(
                    painter = painterResource(R.drawable.edit_image_2),
                    contentDescription = "Edit Button",
                    modifier = Modifier.size(15.dp)
                )
            }
            ProfileItem(R.drawable.human_profile, "Name", username ?: "Loading...")
            ProfileItem(R.drawable.mail_2, "Email", "mugehajacky@gmail.com")
        }

        ProfileSection("Account Info") {
            ProfileItem(R.drawable.settings_black, "Security", "Change Password")
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

        // Logout Button
        FilledButton(
            title = if (isLoggingOut) "Logging out..." else "Log out",
            enabled = !isLoggingOut,
            onClick = {
                isLoggingOut = true
                profileViewModel.logout(navController)
            }
        )

        // Bottom Sheet: Show Camera & Gallery Options
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
    modifier: Modifier = Modifier, // Add Modifier parameter with a default value
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
