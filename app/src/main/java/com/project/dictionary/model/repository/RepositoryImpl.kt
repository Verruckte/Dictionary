package com.project.dictionary.model.repository

import com.project.dictionary.model.data.DataModel
import com.project.dictionary.model.datasource.DataSource

class RepositoryImpl (private val dataSource: DataSource<List<DataModel>>) :
    Repository<List<DataModel>> {

    override suspend fun getData(word: String): List<DataModel>? {
        return dataSource.getData(word)
    }
}