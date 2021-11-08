package com.project.dictionary.model.datasource.database

import com.project.dictionary.model.data.AppState
import com.project.dictionary.model.data.DataModel
import com.project.dictionary.model.datasource.DataSourceLocal
import com.project.dictionary.model.datasource.database.room.HistoryDao
import com.project.dictionary.utils.convertDataModelSuccessToEntity
import com.project.dictionary.utils.mapHistoryEntityToSearchResult

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