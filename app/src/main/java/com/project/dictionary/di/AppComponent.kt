package com.project.dictionary.di

import com.project.dictionary.di.modules.*
import com.project.dictionary.di.modules.ViewModelModule
import com.project.dictionary.view.App
import com.project.dictionary.view.main.MainActivity
import com.project.dictionary.view.wordslist.WordsListFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        InteractorModule::class,
        RepositoryModule::class,
        ViewModelModule::class,
        NavigationModule::class]
)
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: App): Builder

        fun build(): AppComponent
    }

    fun inject(mainActivity: MainActivity)
    fun inject(wordsListFragment: WordsListFragment)

}