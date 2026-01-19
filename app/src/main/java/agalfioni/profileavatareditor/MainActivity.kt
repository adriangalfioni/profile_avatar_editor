package agalfioni.profileavatareditor

import agalfioni.profileavatareditor.ui.theme.ProfileAvatarEditorTheme
import agalfioni.wintertravelgallery.core.navigation.NavigationWrapper
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProfileAvatarEditorTheme {
                NavigationWrapper()
            }
        }
    }
}
