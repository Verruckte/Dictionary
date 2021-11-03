package com.project.dictionary.view.main

import androidx.lifecycle.ViewModel
import com.project.dictionary.navigation.Screens
import ru.terrakok.cicerone.Router

class MainActivityViewModel (private val router: Router ): ViewModel(){

    fun backPressed() {
        router.exit()
    }

    fun onCreate() {
        router.replaceScreen(Screens.WordsListScreen())
    }
}