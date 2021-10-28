package com.project.dictionary.navigation

import com.project.dictionary.view.wordslist.WordsListFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class Screens {

    class WordsListScreen() : SupportAppScreen() {
        override fun getFragment() = WordsListFragment.newInstance()
    }

}