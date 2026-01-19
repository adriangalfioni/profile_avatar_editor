package agalfioni.profileavatareditor.profile.presentation.components

import agalfioni.profileavatareditor.R
import agalfioni.profileavatareditor.ui.theme.ProfileAvatarEditorTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.addLastModifiedToFileCacheKey
import coil3.request.crossfade
import java.io.File

@Composable
fun AvatarPictureContainer(
    modifier: Modifier = Modifier,
    savedImagePath: String? = null
) {
    val context = LocalContext.current

    Box(modifier = modifier
        .size(136.dp)
        .clip(CircleShape)
        .background(color = Color.Transparent)
        .border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline,
            shape = CircleShape
        ),
        contentAlignment = Alignment.Center
    ) {
        if (savedImagePath != null) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(File(savedImagePath)) // Load directly from the File object
                    .addLastModifiedToFileCacheKey(true)
                    .crossfade(true)
                    .build(),
                contentDescription = "User Avatar",
                modifier = Modifier
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(modifier = Modifier
                .clip(CircleShape)
                .fillMaxSize()
                .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier
                        .clip(CircleShape),
                    painter = painterResource(id = R.drawable.profile_placeholder),
                    contentDescription = "profile_image_placeholder"
                )
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = "add_picture",
                    tint = Color.White
                )
            }
        }
    }

}

@Preview
@Composable
private fun AvatarPictureContainerPreview() {
    ProfileAvatarEditorTheme {
        AvatarPictureContainer()
    }
}
