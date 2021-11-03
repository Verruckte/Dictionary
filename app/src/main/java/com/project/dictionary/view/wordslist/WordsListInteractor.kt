package com.project.dictionary.view.wordslist

import com.project.dictionary.model.data.AppState
import com.project.dictionary.model.data.DataModel
import com.project.dictionary.model.repository.Repository
import com.project.dictionary.viewmodel.Interactor

class WordsListInteractor(
    private val remoteRepository: Repository<List<DataModel>>,
    private val localRepository: Repository<List<DataModel>>
) : Interactor<AppState> {

    override suspend fun getData(word: String, fromRemoteSource: Boolean): AppState {
        return if (fromRemoteSource) {
            AppState.Success(remoteRepository.getData(word))
        } else {
            AppState.Success( localRepository.getData(word))
        }
    }
}