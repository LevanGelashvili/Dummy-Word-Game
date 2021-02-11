package com.adjarabet.user.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.adjarabet.user.app.App
import com.adjarabet.user.data.BotGameRepositoryImpl
import com.adjarabet.user.domain.repository.GameRepository
import com.adjarabet.user.domain.usecase.GameLogicUseCase
import com.adjarabet.user.presentation.GameActivity
import com.adjarabet.user.presentation.GameFragment
import com.adjarabet.user.presentation.GameViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideApplicationContext(application: App): Context = application

    @Provides
    @Singleton
    fun provideGameRepository(context: Context): GameRepository {
        return BotGameRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideGameLogicUseCase(): GameLogicUseCase {
        return GameLogicUseCase()
    }
}

@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(
        modules = [
            FragmentBuildersModule::class,
            ViewModelModule::class
        ]
    )
    abstract fun contributeGameActivity(): GameActivity
}

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeGameFragment(): GameFragment
}

@Module
abstract class ViewModelFactoryModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(GameViewModel::class)
    internal abstract fun bindGameViewModel(viewModel: GameViewModel): ViewModel
}