package com.project.dictionary.model.repository

import com.project.dictionary.model.data.DataModel
import com.project.dictionary.model.datasource.DataSource
import io.reactivex.Observable

class RepositoryImpl (private val dataSource: DataSource<List<DataModel>>) :
    Repository<List<DataModel>> {

    override fun getData(word: String): Observable<List<DataModel>> {
        return dataSource.getData(word)
    }
}