package agalfioni.profileavatareditor.avatar_editor.data.repository

import agalfioni.profileavatareditor.avatar_editor.domain.repository.AvatarRepository
import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class AvatarRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AvatarRepository {

    companion object {
        private const val PREFS_NAME = "user_prefs"
        private const val AVATAR_FILENAME = "profile_avatar.jpg"
        private const val KEY_AVATAR_PATH = "avatar_local_path"
    }

    private val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override suspend fun saveAvatarBitmap(bitmap: Bitmap): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val file = File(context.filesDir, AVATAR_FILENAME)

                FileOutputStream(file).use { stream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
                }
                Result.success(file.absolutePath)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override fun saveAvatarPath(path: String) {
        sharedPrefs.edit {
            putString(KEY_AVATAR_PATH, path)
        }
    }

    override fun getAvatarPath(): String? {
        return sharedPrefs.getString(KEY_AVATAR_PATH, null)
    }
}
