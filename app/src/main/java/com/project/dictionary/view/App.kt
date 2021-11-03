package com.project.dictionary.view

import android.app.Application
import com.project.dictionary.di.modules.*
import org.koin.core.context.startKoin

class App: Application() {

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin {
            modules(listOf(application, viewModelModule, navigation, mainActivity, wordsListScreen))
        }
    }

}