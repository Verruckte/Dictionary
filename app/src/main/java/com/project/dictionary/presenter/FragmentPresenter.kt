package com.project.dictionary.presenter

import com.project.dictionary.model.data.AppState
import com.project.dictionary.model.data.DataModel
import com.project.dictionary.view.base.View

interface FragmentPresenter<T : AppState, V : View> {

    fun attachView(view: V)

    fun detachView(view: V)

    fun getData(word: String, isOnline: Boolean)

    fun backClick(): Boolean

    fun dataObtained(dataModel: List<DataModel>)

}