package com.project.dictionary.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.project.dictionary.R
import com.project.dictionary.di.modules.injectDependencies
import org.koin.android.ext.android.getKoin
import org.koin.core.qualifier.named
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator

class MainActivity : AppCompatActivity() {

    val navigatorHolder: NavigatorHolder by lazy { getKoin().get<NavigatorHolder>() }
    val navigator = SupportAppNavigator(this, supportFragmentManager, R.id.container)

    lateinit var model: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViewModel()
        if(savedInstanceState == null) model.onCreate()
    }

    private fun initViewModel() {
        injectDependencies()
        val factory = getKoin().get<ViewModelProvider.Factory>(qualifier = named("appViewModelProvider"))
        val viewModel = ViewModelProvider(this, factory).get(MainActivityViewModel::class.java)
        model = viewModel
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }

    override fun onBackPressed() {
        supportFragmentManager.fragments.forEach {
            if (it is com.project.core.BackButtonListener && it.backPressed()) {
                return
            }
        }
        model.backPressed()
    }

}