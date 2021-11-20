package com.project.historyscreen.historyscreen

import androidx.lifecycle.LiveData
import com.project.model.data.AppState
import com.project.model.data.DataModel
import com.project.core.viewmodel.BaseViewModel
import com.project.historyscreen.convertMeaningsToString
import com.project.historyscreen.navigation.Screens
import com.project.historyscreen.parseLocalSearchResults
import kotlinx.coroutines.launch
import ru.terrakok.cicerone.Router

class HistoryViewModel(private val interactor: HistoryInteractor, private val router: Router) :
    BaseViewModel<AppState>() {

    private val liveData: LiveData<AppState> = liveDataForViewToObserve

    fun subscribe(): LiveData<AppState> {
        return liveData
    }

    override fun getData(word: String, isOnline: Boolean) {
        liveDataForViewToObserve.value = AppState.Loading(null)
        cancelJob()
        viewModelCoroutineScope.launch { startInteractor(word, isOnline) }
    }

    private suspend fun startInteractor(word: String, isOnline: Boolean) {
        liveDataForViewToObserve.postValue(parseLocalSearchResults(interactor.getData(word, isOnline)))
    }

    fun wordClicked(data: DataModel){
        router.navigateTo(
            Screens.DescriptionScreen(
                data.text!!,
                convertMeaningsToString(data.meanings!!),
                null
            ))
        println("Meanings = ${data.meanings}")
    }

    override fun handleError(error: Throwable) {
        liveDataForViewToObserve.postValue(AppState.Error(error))
    }

    override fun onCleared() {
        liveDataForViewToObserve.value = AppState.Success(null)//Set View to original state in onStop
        super.onCleared()
    }

    override fun backPressed(): Boolean {
        router.exit()
        return true
    }
}