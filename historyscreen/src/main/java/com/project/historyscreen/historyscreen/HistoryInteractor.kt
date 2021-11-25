package com.project.historyscreen.historyscreen

import com.project.model.data.AppState
import com.project.model.data.DataModel
import com.project.repository.repository.Repository
import com.project.repository.repository.RepositoryLocal
import com.project.core.viewmodel.Interactor

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