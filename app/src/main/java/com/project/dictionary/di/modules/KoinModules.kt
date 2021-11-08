package com.project.dictionary.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.project.dictionary.di.ViewModelFactory
import com.project.dictionary.model.data.DataModel
import com.project.dictionary.model.datasource.network.RetrofitImpl
import com.project.dictionary.model.datasource.database.RoomDataBaseImpl
import com.project.dictionary.model.datasource.database.room.HistoryDao
import com.project.dictionary.model.datasource.database.room.HistoryDatabase
import com.project.dictionary.model.repository.Repository
import com.project.dictionary.model.repository.RepositoryImpl
import com.project.dictionary.model.repository.RepositoryLocal
import com.project.dictionary.model.repository.RepositoryLocalImpl
import com.project.dictionary.view.historyscreen.HistoryInteractor
import com.project.dictionary.view.historyscreen.HistoryViewModel
import com.project.dictionary.view.main.MainActivityViewModel
import com.project.dictionary.view.wordslist.WordsListInteractor
import com.project.dictionary.view.wordslist.WordsListViewModel
import org.koin.dsl.module
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import javax.inject.Provider

val application = module {
    single { Room.databaseBuilder(get(), HistoryDatabase::class.java, "HistoryDB").build() }
    single<HistoryDao> { get<HistoryDatabase>().historyDao() }
    single<Repository<List<DataModel>>> { RepositoryImpl(RetrofitImpl()) }
    single<RepositoryLocal<List<DataModel>>> { RepositoryLocalImpl(RoomDataBaseImpl(get())) }
}

val viewModelModule = module {
    single<MutableMap<Class<out ViewModel>, Provider<ViewModel>>> {
        var map =
            mutableMapOf(
                MainActivityViewModel::class.java to Provider<ViewModel>{MainActivityViewModel(get<Router>())},
                WordsListViewModel::class.java to Provider<ViewModel>{WordsListViewModel(get<WordsListInteractor>(), get<Router>()) },
                HistoryViewModel::class.java to Provider<ViewModel>{HistoryViewModel(get<HistoryInteractor>(), get<Router>()) })
        map
    }
    single<ViewModelProvider.Factory> { ViewModelFactory(get())}
}

val navigation = module {
    val cicerone: Cicerone<Router> = Cicerone.create()
    factory<NavigatorHolder> { cicerone.navigatorHolder }
    factory<Router> { cicerone.router }
}

val mainActivity = module {
    factory { MainActivityViewModel(get<Router>()) }
}

val wordsListScreen = module {
    factory { WordsListInteractor(get(), get()) }
    factory { WordsListViewModel(get<WordsListInteractor>(), get<Router>()) }
}

val historyScreen = module {
    factory { HistoryViewModel(get(), get()) }
    factory { HistoryInteractor(get(), get()) }
}