package com.project.dictionary.view.historyscreen

import com.project.dictionary.model.data.AppState
import com.project.dictionary.model.data.DataModel
import com.project.dictionary.model.repository.Repository
import com.project.dictionary.model.repository.RepositoryLocal
import com.project.dictionary.viewmodel.Interactor

class HistoryInteractor(
    private val repositoryRemote: Repository<List<DataModel>>,
    private val repositoryLocal: RepositoryLocal<List<DataModel>>
) : Interactor<AppState> {

    override suspend fun getData(word: String, fromRemoteSource: Boolean): AppState {
        return AppState.Success(
            if (fromRemoteSource) {
                repositoryRemote
            } else {
                repositoryLocal
            }.getData(word)
        )
    }
}