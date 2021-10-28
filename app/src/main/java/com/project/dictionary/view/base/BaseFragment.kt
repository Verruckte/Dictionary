package com.project.dictionary.view.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.project.dictionary.model.data.AppState
import com.project.dictionary.presenter.FragmentPresenter


abstract class BaseFragment<T : AppState> : Fragment(), View {

    protected lateinit var fragmentPresenter: FragmentPresenter<T, View>

    protected abstract fun createPresenter(): FragmentPresenter<T, View>

    abstract override fun renderData(appState: AppState)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentPresenter = createPresenter()
    }

    override fun onStart() {
        super.onStart()
        fragmentPresenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        fragmentPresenter.detachView(this)
    }
}