package com.project.dictionary.model.datasource

import com.project.dictionary.model.data.AppState

interface DataSourceLocal<T> : DataSource<T> {

    suspend fun saveToDB(appState: AppState)

}