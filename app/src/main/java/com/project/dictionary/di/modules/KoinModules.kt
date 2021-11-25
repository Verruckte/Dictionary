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
import com.project.dictionary.view.main.MainActivityViewModel
import com.project.wordslistscreen.wordslist.WordsListFragment
import com.project.wordslistscreen.wordslist.WordsListInteractor
import com.project.wordslistscreen.wordslist.WordsListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import javax.inject.Provider

fun injectDependencies() = loadModules

private val loadModules by lazy {
    loadKoinModules(listOf(application, viewModelModule, navigation, mainActivity, wordsListScreen))
}

val application = module {
    single { Room.databaseBuilder(get(), HistoryDatabase::class.java, "HistoryDB").build() }
    single<HistoryDao> { get<HistoryDatabase>().historyDao() }
    single<Repository<List<DataModel>>> { RepositoryImpl(RetrofitImpl()) }
    single<RepositoryLocal<List<DataModel>>> { RepositoryLocalImpl(RoomDataBaseImpl(get())) }
}

val viewModelModule = module {
    single<MutableMap<Class<out ViewModel>, Provider<ViewModel>>>(qualifier = named("appViewModelMap")) {
        var map =
            mutableMapOf(
                MainActivityViewModel::class.java to Provider<ViewModel>{MainActivityViewModel(get<Router>())},
                WordsListViewModel::class.java to Provider<ViewModel>{WordsListViewModel(get<WordsListInteractor>(), get<Router>()) })
        map
    }
    single<ViewModelProvider.Factory>(qualifier = named("appViewModelProvider")) {
        ViewModelFactory(get<MutableMap<Class<out ViewModel>, Provider<ViewModel>>>(
            qualifier = named("appViewModelMap")))}
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
    scope(named<WordsListFragment>()) {
        scoped { WordsListInteractor(get(), get()) }
        viewModel { WordsListViewModel(get<WordsListInteractor>(), get<Router>()) }
    }
}