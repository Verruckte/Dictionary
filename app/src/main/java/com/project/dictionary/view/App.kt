package com.project.dictionary.view

import android.app.Application
import com.project.dictionary.di.AppComponent
import com.project.dictionary.di.DaggerAppComponent

class App: Application() {

    companion object {
        lateinit var instance: App
    }

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()
    }

}