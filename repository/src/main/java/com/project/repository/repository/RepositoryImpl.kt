package com.project.repository.repository

import com.project.model.data.DataModel
import com.project.repository.datasource.DataSource
import com.project.repository.repository.Repository

class RepositoryImpl (private val dataSource: DataSource<List<DataModel>>) :
    Repository<List<DataModel>> {

    override suspend fun getData(word: String): List<DataModel>? {
        return dataSource.getData(word)
    }
}