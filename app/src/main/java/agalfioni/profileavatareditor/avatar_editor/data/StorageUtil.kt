package agalfioni.profileavatareditor.avatar_editor.data

import android.content.Context
import android.graphics.Bitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import androidx.core.content.edit

object StorageUtil {

    // 1. Save Bitmap to Internal Storage
    suspend fun saveToInternalStorage(context: Context, bitmap: Bitmap): String {
        return withContext(Dispatchers.IO) {
            // Create a file in the app's private internal storage
            val filename = "profile_avatar.jpg"
            val file = File(context.filesDir, filename)

            // Compress and write the bitmap
            FileOutputStream(file).use { stream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
            }

            // Return the absolute path so we can save it to preferences
            file.absolutePath
        }
    }

    // 2. Save the Path to SharedPreferences
    fun savePathToPreferences(context: Context, path: String) {
        val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        sharedPref.edit {
            putString("avatar_local_path", path)
        }
    }

    // 3. Get the Path from SharedPreferences
    fun getSavedAvatarPath(context: Context): String? {
        val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPref.getString("avatar_local_path", null)
    }
}