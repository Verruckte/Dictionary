package com.project.historyscreen.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.dictionary.di.ViewModelFactory
import com.project.dictionary.view.main.MainActivityViewModel
import com.project.historyscreen.historyscreen.HistoryFragment
import com.project.historyscreen.historyscreen.HistoryInteractor
import com.project.historyscreen.historyscreen.HistoryViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.terrakok.cicerone.Router
import javax.inject.Provider


fun injectDependencies() = loadFeature

private val loadFeature by lazy {
    loadKoinModules(listOf(viewModelModule, historyScreen))
}

val viewModelModule = module {
    single<MutableMap<Class<out ViewModel>, Provider<ViewModel>>>(qualifier = named("historyViewModelMap")) {
        val map =
            mutableMapOf(
                MainActivityViewModel::class.java to Provider<ViewModel>{ MainActivityViewModel(get<Router>()) },

                HistoryViewModel::class.java to Provider<ViewModel>{HistoryViewModel(get<HistoryInteractor>(), get<Router>()) })
        map
    }
    single<ViewModelProvider.Factory>(qualifier = named("historyViewModelProvider")) { ViewModelFactory(get<MutableMap<Class<out ViewModel>, Provider<ViewModel>>>(
        qualifier = named("historyViewModelMap")))
    }
}

val historyScreen = module {
    scope(named<HistoryFragment>()){
        viewModel { HistoryViewModel(get(), get()) }
        scoped { HistoryInteractor(get(), get()) }
    }

}