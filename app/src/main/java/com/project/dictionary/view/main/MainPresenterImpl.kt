package com.project.dictionary.view.main

import com.project.dictionary.navigation.Screens
import com.project.dictionary.presenter.MainActivityPresenter
import com.project.dictionary.view.App
import com.project.dictionary.view.base.View
import ru.terrakok.cicerone.Router

class MainPresenterImpl<V: View>( private val router: Router = App.instance.router): MainActivityPresenter<V> {

    override fun backClick() {
        router.exit()
    }

    override fun onCreate() {
        router.replaceScreen(Screens.WordsListScreen())
    }

    private var currentView: V? = null

    override fun attachView(view: V) {
        if (view != currentView) {
            currentView = view
        }
    }

    override fun detachView(view: V) {
        if (view == currentView) {
            currentView = null
        }
    }
}