package com.project.dictionary.model.repository

import com.project.dictionary.model.data.AppState

interface RepositoryLocal<T> : Repository<T> {

    suspend fun saveToDB(appState: AppState)

}