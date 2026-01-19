package agalfioni.profileavatareditor.core.navigation

import agalfioni.profileavatareditor.avatar_editor.presentation.AvatarEditorScreen
import agalfioni.profileavatareditor.profile.presentation.ProfileScreen
import agalfioni.wintertravelgallery.core.navigation.Route
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay

@Composable
fun NavigationWrapper(
    modifier: Modifier = Modifier
) {
    val backStack = remember { mutableStateListOf<NavKey>(Route.ProfileScreen) }
    val resultStore = rememberResultStore()

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                is Route.ProfileScreen -> NavEntry(key) {
                    ProfileScreen(
                        resultStore = resultStore,
                        onPictureSelected = { uri ->
                            backStack.add(Route.AvatarEditor(uri))
                        }
                    )
                }

                is Route.AvatarEditor -> NavEntry(key) {
                    AvatarEditorScreen(
                        resultStore = resultStore,
                        imageUri = key.uri,
                        onImageCropped = { backStack.removeLastOrNull() }
                    )
                }

                else -> {
                    error("Unknown route: $key")
                }
            }
        }
    )

}