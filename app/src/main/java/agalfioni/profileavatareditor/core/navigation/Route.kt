package agalfioni.wintertravelgallery.core.navigation

import android.net.Uri
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route: NavKey {

    @Serializable
    data object ProfileScreen: Route

    @Serializable
    data class AvatarEditor(val uri: Uri): Route
}