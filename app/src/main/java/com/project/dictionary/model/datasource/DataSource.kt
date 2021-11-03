package com.project.dictionary.model.datasource


interface DataSource<T> {

    suspend fun getData(word: String): T
}