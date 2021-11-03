package com.project.dictionary.model.repository

interface Repository<T> {

    suspend fun getData(word: String): T
}