package com.project.repository.datasource.database

import com.project.repository.datasource.DataSourceLocal
import com.project.model.data.AppState
import com.project.model.data.DataModel
import com.project.repository.convertDataModelSuccessToEntity
import com.project.repository.datasource.database.room.HistoryDao
import com.project.repository.mapHistoryEntityToSearchResult

class RoomDataBaseImpl(private val historyDao: HistoryDao) :
    DataSourceLocal<List<DataModel>> {

    override suspend fun getData(word: String): List<DataModel>? {
        if (word == "") return mapHistoryEntityToSearchResult(historyDao.all())
        val historyEntityResult = historyDao.getDataByWord(word)
        return historyEntityResult?.let { mapHistoryEntityToSearchResult(listOf(it)) }
    }

    override suspend fun saveToDB(appState: AppState) {
        convertDataModelSuccessToEntity(appState)?.let {
            historyDao.insert(it)
        }
    }
}