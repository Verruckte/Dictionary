package com.project.dictionary.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.project.dictionary.di.ViewModelFactory
import com.project.model.data.DataModel
import com.project.repository.datasource.network.RetrofitImpl
import com.project.repository.datasource.database.RoomDataBaseImpl
import com.project.repository.datasource.database.room.HistoryDao
import com.project.repository.datasource.database.room.HistoryDatabase
import com.project.repository.repository.Repository
import com.project.repository.repository.RepositoryImpl
import com.project.repository.repository.RepositoryLocal
import com.project.repository.repository.RepositoryLocalImpl
import com.project.historyscreen.historyscreen.HistoryInteractor
import com.project.historyscreen.historyscreen.HistoryViewModel
import com.project.dictionary.view.main.MainActivityViewModel
import com.project.wordslistscreen.wordslist.WordsListInteractor
import com.project.wordslistscreen.wordslist.WordsListViewModel
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
                HistoryViewModel::class.java to Provider<ViewModel>{ HistoryViewModel(get<HistoryInteractor>(), get<Router>()) })
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