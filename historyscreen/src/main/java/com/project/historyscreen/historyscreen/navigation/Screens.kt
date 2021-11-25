package com.project.historyscreen.historyscreen.navigation

import ru.terrakok.cicerone.android.support.SupportAppScreen

class Screens {


    class DescriptionScreen(val word: String, val description: String, val pictureUrl: String?): SupportAppScreen() {
        override fun getFragment() = com.project.descriptionscreen.DescriptionFragment.newInstance(word, description, pictureUrl)
    }



}