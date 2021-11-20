package com.project.wordslistscreen.wordslist

import com.project.model.data.AppState
import com.project.model.data.DataModel
import com.project.repository.repository.Repository
import com.project.repository.repository.RepositoryLocal
import com.project.core.viewmodel.Interactor

class WordsListInteractor(
    private val remoteRepository: Repository<List<DataModel>>,
    private val localRepository: RepositoryLocal<List<DataModel>>
) : Interactor<AppState> {

    override suspend fun getData(word: String, fromRemoteSource: Boolean): AppState {
        val appState: AppState
        if (fromRemoteSource) {
            appState = AppState.Success(remoteRepository.getData(word))
            localRepository.saveToDB(appState)
        } else {
            appState = AppState.Success( localRepository.getData(word))
        }
        return appState
    }
}