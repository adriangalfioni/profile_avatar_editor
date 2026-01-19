package agalfioni.profileavatareditor.avatar_editor.domain.repository

import android.graphics.Bitmap

interface AvatarRepository {
    suspend fun saveAvatarBitmap(bitmap: Bitmap): Result<String>
    fun saveAvatarPath(path: String)
    fun getAvatarPath(): String?
}
