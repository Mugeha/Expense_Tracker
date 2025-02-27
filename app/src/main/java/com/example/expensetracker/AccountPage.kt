import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.expensetracker.FilledButton
import com.example.expensetracker.R

@Composable
fun ProfileScreen(navController: NavController) {
    var isDarkMode by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 28.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally // Centers content
    ) {
        // Back Button (Aligned Left)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.back_button), // Replace with your image
                contentDescription = "Back",
                modifier = Modifier
                    .size(64.dp).padding(top = 26.dp)
                    .clickable { navController.popBackStack() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Picture - Centered
        // Profile Picture - Centered
        Box(
            modifier = Modifier.size(150.dp), // Ensure Box size matches the profile image
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.human_profile), // Replace with your image
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape), // Ensures a circular profile picture
                contentScale = ContentScale.Crop
            )

            Image(
                painter = painterResource(id = R.drawable.camera_pic_removebg_preview), // Replace with your edit icon
                contentDescription = "Edit Image Profile",
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.BottomEnd) // Moves it to the bottom-right
                    .offset(x = (-10).dp, y = (-12).dp) // Fine-tunes position slightly inside the profile image
                    .clickable { /* Handle edit profile */ }
                    .alpha(0.9f) // Set opacity to 80% (0.8)
            )
        }


        // Profile Name
        Text(
            text = "Mugeha Jackline",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Sections
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            ProfileSection("Personal Info") {
                           Spacer(modifier = Modifier.height(6.dp))
                Row (horizontalArrangement = Arrangement.spacedBy(6.dp)){
                    Text(text = "Edit", color = Color.Black)

                    Image(
                        painter = painterResource(R.drawable.edit_image_2),
                        contentDescription = "Edit Button",
                        modifier = Modifier.size(15.dp)
                    )
                }
                ProfileItem(R.drawable.human_profile, "Name", "Mugeha Jackline", modifier = Modifier.size(48.dp)) // Override image size here)
                ProfileItem(R.drawable.mail_2, "Email", "mugehajacky@gmail.com")
            }
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
        FilledButton(title = "Log out", destination = "login-screen", navController = rememberNavController())
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


@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    val navController = rememberNavController()
    ProfileScreen(navController = navController)
}
