package com.project.dictionary.di.modules

import com.project.dictionary.di.NAME_LOCAL
import com.project.dictionary.di.NAME_REMOTE
import com.project.dictionary.model.data.DataModel
import com.project.dictionary.model.repository.Repository
import com.project.dictionary.view.wordslist.WordsListInteractor
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class InteractorModule {

    @Provides
    internal fun provideInteractor(
        @Named(NAME_REMOTE) repositoryRemote: Repository<List<DataModel>>,
        @Named(NAME_LOCAL) repositoryLocal: Repository<List<DataModel>>
    ) = WordsListInteractor(repositoryRemote, repositoryLocal)
}