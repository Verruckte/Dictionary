package com.project.repository.repository

import com.project.model.data.DataModel

interface Repository<T> {

    suspend fun getData(word: String): List<DataModel>?
}