package com.project.dictionary.view.base

import com.project.dictionary.model.data.AppState

interface View {

    fun renderData(appState: AppState)

}