package agalfioni.profileavatareditor.di

import agalfioni.profileavatareditor.avatar_editor.data.repository.AvatarRepositoryImpl
import agalfioni.profileavatareditor.avatar_editor.domain.repository.AvatarRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindAvatarRepository(
        avatarRepositoryImpl: AvatarRepositoryImpl
    ): AvatarRepository
}
