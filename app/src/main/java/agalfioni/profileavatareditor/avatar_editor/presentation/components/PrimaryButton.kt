package agalfioni.profileavatareditor.avatar_editor.presentation.components

import agalfioni.profileavatareditor.ui.theme.PrimaryGradient
import agalfioni.profileavatareditor.ui.theme.ProfileAvatarEditorTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    isEnabled: Boolean = true
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = Color.Transparent,
        ),
        modifier = modifier.background(
            brush = if (isEnabled) {
                PrimaryGradient
            } else {
                SolidColor(MaterialTheme.colorScheme.surfaceVariant)
            },
            shape = CircleShape
        )
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontSize = 16.sp
            )
        )
    }
}

@Preview
@Composable
fun PrimaryButtonPreview() {
    ProfileAvatarEditorTheme {
        PrimaryButton(text = "Primary Button")
    }
}

