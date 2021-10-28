package com.project.dictionary.presenter

import com.project.dictionary.view.base.View

interface MainActivityPresenter<V: View> {

    fun attachView(view: V)

    fun detachView(view: V)

    fun onCreate()

    fun backClick()
}