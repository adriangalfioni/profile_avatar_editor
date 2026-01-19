package agalfioni.profileavatareditor.profile.presentation

import agalfioni.profileavatareditor.avatar_editor.domain.repository.AvatarRepository
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: AvatarRepository
) : ViewModel() {

    private val _avatarPath = mutableStateOf<String?>(null)
    val avatarPath: State<String?> = _avatarPath

    init {
        loadAvatarPath()
    }

    fun loadAvatarPath() {
        _avatarPath.value = repository.getAvatarPath()
    }
}
