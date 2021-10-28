package com.project.dictionary.view.wordslist

import com.project.dictionary.model.data.AppState
import com.project.dictionary.model.data.DataModel
import com.project.dictionary.model.repository.Repository
import com.project.dictionary.presenter.Interactor
import io.reactivex.Observable

class WordsListInteractor(
    private val remoteRepository: Repository<List<DataModel>>,
    private val localRepository: Repository<List<DataModel>>
) : Interactor<AppState> {

    override fun getData(word: String, fromRemoteSource: Boolean): Observable<AppState> {
        return if (fromRemoteSource) {
            remoteRepository.getData(word).map { AppState.Success(it) }
        } else {
            localRepository.getData(word).map { AppState.Success(it) }
        }
    }
}