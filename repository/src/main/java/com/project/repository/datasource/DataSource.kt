package com.project.repository.datasource

import com.project.model.data.DataModel


interface DataSource<T> {

    suspend fun getData(word: String): List<DataModel>?
}