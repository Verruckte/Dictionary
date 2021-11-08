package com.project.dictionary.model.datasource

import com.project.dictionary.model.data.DataModel


interface DataSource<T> {

    suspend fun getData(word: String): List<DataModel>?
}