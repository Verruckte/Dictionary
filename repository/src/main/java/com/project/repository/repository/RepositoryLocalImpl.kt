package com.project.repository.repository

import com.project.model.data.AppState
import com.project.model.data.DataModel
import com.project.repository.datasource.DataSourceLocal
import com.project.repository.repository.RepositoryLocal

class RepositoryLocalImpl(private val dataSource: DataSourceLocal<List<DataModel>>) :
    RepositoryLocal<List<DataModel>> {

    override suspend fun getData(word: String): List<DataModel>? {
        return dataSource.getData(word)
    }

    override suspend fun saveToDB(appState: AppState) {
        dataSource.saveToDB(appState)
    }
}