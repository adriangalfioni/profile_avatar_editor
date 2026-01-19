package agalfioni.profileavatareditor.profile.presentation.components

import agalfioni.profileavatareditor.R
import agalfioni.profileavatareditor.avatar_editor.data.StorageUtil
import agalfioni.profileavatareditor.core.navigation.ResultStore
import agalfioni.profileavatareditor.ui.theme.PlusJakartaSans
import agalfioni.profileavatareditor.ui.theme.ProfileAvatarEditorTheme
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onPictureSelected: (Uri) -> Unit = {},
    resultStore: ResultStore? = null,
) {
    // 1. Create the state to manage the snackbar
    val snackbarHostState = remember { SnackbarHostState() }

    // 2. Create a coroutine scope to launch the suspend function
    val scope = rememberCoroutineScope()

    val navigationIconSize = 40.dp
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
                navigationIcon = {
                    OutlinedButton(
                        onClick = {},
                        modifier= Modifier.size(navigationIconSize),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        contentPadding = PaddingValues(0.dp),  //avoid the little icon
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = colorResource(R.color.white))
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_left),
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = navigationIconSize), // compensate nav icon
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Profile",
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                        )
                    }
                },
                windowInsets = WindowInsets.statusBars.add(WindowInsets(top = 32.dp)),
            )
        }
    ) { innerPadding ->

        // Registers a photo picker activity launcher in single-select mode.
        val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                onPictureSelected(uri)
            }
        }

        val context = LocalContext.current

        // 1. Retrieve the path from Preferences
        // In a real app, you might want to observe this as State or Flow
        val avatarImagePath = remember { StorageUtil.getSavedAvatarPath(context) }

        val croppedImage = resultStore?.getResult<Bitmap>("cropped_image")

        LaunchedEffect(avatarImagePath, croppedImage) {
            if (avatarImagePath != null && croppedImage != null) {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Avatar updated successfully",
                        duration = SnackbarDuration.Long
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AvatarPictureContainer(
                savedImagePath = avatarImagePath
            )
            Spacer(modifier = Modifier.height(12.dp))
            ActionButton(
                text = "Change Avatar",
                onClick = {
                    // Launch the photo picker and let the user choose only images.
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Jonh Doe",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "john.doe@example.com",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            OptionsButtonMenu(modifier = Modifier.padding(horizontal = 16.dp))
        }
    }
}

@Composable
fun OptionsButtonMenu(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        MenuCard {
            MenuItem(
                icon = Icons.Outlined.Person,
                text = "Profile details",
                onClick = { /* Handle click */ }
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
            MenuItem(
                icon = Icons.Outlined.Lock,
                text = "Login & security",
                onClick = { /* Handle click */ }
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
            MenuItem(
                icon = Icons.Outlined.Notifications,
                text = "Notifications",
                showChevron = true, // Explicitly showing logic
                onClick = { /* Handle click */ }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Card 2: Logout
        MenuCard {
            MenuItem(
                icon = Icons.AutoMirrored.Filled.ExitToApp,
                text = "Log out",
                isDestructive = true,
                showChevron = false,
                onClick = { /* Handle logout */ }
            )
        }
    }

}

@Preview
@Composable
private fun ProfileScreenPreview() {
    ProfileAvatarEditorTheme {
        ProfileScreen()
    }
}