package com.project.wordslistscreen.wordslist

import androidx.lifecycle.LiveData
import com.project.model.data.AppState
import com.project.model.data.DataModel
import com.project.core.viewmodel.BaseViewModel
import com.project.wordslistscreen.convertMeaningsToString
import com.project.wordslistscreen.navigation.Screens
import com.project.wordslistscreen.parseSearchResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.terrakok.cicerone.Router

class WordsListViewModel ( private val interactor: WordsListInteractor,
                           private val router: Router): BaseViewModel<AppState>()  {

    fun subscribe(): LiveData<AppState> = liveDataForViewToObserve

    override fun getData(word: String, isOnline: Boolean) {
        liveDataForViewToObserve.value = AppState.Loading(null)
        cancelJob()
        viewModelCoroutineScope.launch {
            getDataFromInteractor(word, isOnline)
        }
    }

    //Doesn't have to use withContext for Retrofit call if you use .addCallAdapterFactory(CoroutineCallAdapterFactory()). The same goes for Room
    private suspend fun getDataFromInteractor(word: String, isOnline: Boolean) =
        withContext(Dispatchers.IO) {
            liveDataForViewToObserve.postValue(parseSearchResults(interactor.getData(word, isOnline)))
        }

    override fun backPressed(): Boolean {
        router.exit()
        return true
    }

    override fun handleError(error: Throwable) {
        liveDataForViewToObserve.postValue(AppState.Error(error))
    }

    override fun onCleared() {
        liveDataForViewToObserve.value = AppState.Success(null)
        super.onCleared()
    }

    fun wordClicked(data: DataModel){
        router.navigateTo(
            Screens.DescriptionScreen(
                data.text!!,
                convertMeaningsToString(data.meanings!!),
                data.meanings!![0].imageUrl
            ))
        println("PICTURE URL = ${data.meanings!![0].imageUrl}")
    }

    fun historyMenuItemClicked() {
        router.navigateTo(Screens.HistoryScreen())
    }
}