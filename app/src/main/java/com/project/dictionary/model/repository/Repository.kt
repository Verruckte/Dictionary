package com.project.dictionary.model.repository

import com.project.dictionary.model.data.DataModel

interface Repository<T> {

    suspend fun getData(word: String): List<DataModel>?
}