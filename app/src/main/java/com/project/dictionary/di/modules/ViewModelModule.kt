package com.project.dictionary.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.dictionary.di.ViewModelFactory
import com.project.dictionary.di.ViewModelKey
import com.project.dictionary.view.main.MainActivityViewModel
import com.project.dictionary.view.wordslist.WordsListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module(includes = [InteractorModule::class])
internal abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    protected abstract fun mainActivityViewModel(mainViewModel: MainActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WordsListViewModel::class)
    protected abstract fun wordsListViewModel(wordsListViewModel: WordsListViewModel): ViewModel

}