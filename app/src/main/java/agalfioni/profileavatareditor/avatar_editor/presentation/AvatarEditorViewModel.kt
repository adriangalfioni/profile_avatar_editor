package agalfioni.profileavatareditor.avatar_editor.presentation

import agalfioni.profileavatareditor.avatar_editor.domain.repository.AvatarRepository
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AvatarEditorViewModel @Inject constructor(
    private val repository: AvatarRepository
) : ViewModel() {

    private val _onSaveSuccess = MutableSharedFlow<Unit>()
    val onSaveSuccess = _onSaveSuccess.asSharedFlow()

    fun saveCroppedImage(bitmap: Bitmap) {
        viewModelScope.launch {
            repository.saveAvatarBitmap(bitmap).onSuccess { path ->
                repository.saveAvatarPath(path)
                _onSaveSuccess.emit(Unit)
            }
        }
    }
}
