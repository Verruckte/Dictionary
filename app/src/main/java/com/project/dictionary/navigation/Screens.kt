package com.project.dictionary.navigation

import com.project.historyscreen.historyscreen.HistoryFragment
import com.project.wordslistscreen.wordslist.WordsListFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class Screens {

    class WordsListScreen() : SupportAppScreen() {
        override fun getFragment() = WordsListFragment.newInstance()
    }

    class DescriptionScreen(val word: String, val description: String, val pictureUrl: String?): SupportAppScreen() {
        override fun getFragment() = com.project.descriptionscreen.DescriptionFragment.newInstance(word, description, pictureUrl)
    }

    class HistoryScreen() : SupportAppScreen() {
        override fun getFragment() = HistoryFragment.newInstance()
    }

}