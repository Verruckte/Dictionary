package com.project.dictionary.view.wordslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.inflate
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.dictionary.R
import com.project.dictionary.model.data.AppState
import com.project.dictionary.model.data.DataModel
import com.project.dictionary.presenter.FragmentPresenter
import com.project.dictionary.view.App
import com.project.dictionary.view.BackButtonListener
import com.project.dictionary.view.base.BaseFragment
import com.project.dictionary.view.base.View
import com.project.dictionary.view.wordslist.adapter.WordsListRVAdapter
import kotlinx.android.synthetic.main.fragment_words_list.*

class WordsListFragment : BaseFragment<AppState>(), BackButtonListener {

    private var adapter: WordsListRVAdapter? = null
    private val onListItemClickListener: WordsListRVAdapter.OnListItemClickListener =
        object : WordsListRVAdapter.OnListItemClickListener {
            override fun onItemClick(data: DataModel) {
                // some logic
            }
        }

    companion object {
        fun newInstance() = WordsListFragment()
        private const val BOTTOM_SHEET_FRAGMENT_DIALOG_TAG = "12345"
    }

    override fun createPresenter(): FragmentPresenter<AppState, View> {
        return App.instance.presenterHolder.getWordsListPresenter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View? = inflate(context, R.layout.fragment_words_list, null)


    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        search_fab.setOnClickListener {
            val searchDialogFragment = SearchDialogFragment.newInstance()
            searchDialogFragment.setOnSearchClickListener(object : SearchDialogFragment.OnSearchClickListener {
                override fun onClick(searchWord: String) {
                    fragmentPresenter.getData(searchWord, true)
                }
            })
            searchDialogFragment.show(childFragmentManager, BOTTOM_SHEET_FRAGMENT_DIALOG_TAG)
        }
    }

    override fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                val dataModel = appState.data
                if (dataModel == null || dataModel.isEmpty()) {
                    showErrorScreen(getString(R.string.empty_server_response_on_success))
                } else {
                    showViewSuccess()
                    if (adapter == null) {
                        main_activity_recyclerview.layoutManager = LinearLayoutManager(context)
                        main_activity_recyclerview.adapter = WordsListRVAdapter(onListItemClickListener, dataModel)
                    } else {
                        adapter!!.setData(dataModel)
                    }
                    fragmentPresenter.dataObtained(dataModel)
                }
            }
            is AppState.Loading -> {
                showViewLoading()
                if (appState.progress != null) {
                    progress_bar_horizontal.visibility = android.view.View.VISIBLE
                    progress_bar_round.visibility = android.view.View.GONE
                    progress_bar_horizontal.progress = appState.progress
                } else {
                    progress_bar_horizontal.visibility = android.view.View.GONE
                    progress_bar_round.visibility = android.view.View.VISIBLE
                }
            }
            is AppState.Error -> {
                showErrorScreen(appState.error.message)
            }
        }
    }

    override fun onDestroy() {
        // here will be logic, based on FragmentLifecycleCallbacks
        if(activity?.isFinishing == true) App.instance.presenterHolder.clearWordsListPresenter()
        super.onDestroy()
    }

    private fun showErrorScreen(error: String?) {
        showViewError()
        error_textview.text = error ?: getString(R.string.undefined_error)
        reload_button.setOnClickListener {
            fragmentPresenter.getData("hi", true)
        }
    }

    private fun showViewSuccess() {
        success_linear_layout.visibility = android.view.View.VISIBLE
        loading_frame_layout.visibility = android.view.View.GONE
        error_linear_layout.visibility = android.view.View.GONE
    }

    private fun showViewLoading() {
        success_linear_layout.visibility = android.view.View.GONE
        loading_frame_layout.visibility = android.view.View.VISIBLE
        error_linear_layout.visibility = android.view.View.GONE
    }

    private fun showViewError() {
        success_linear_layout.visibility = android.view.View.GONE
        loading_frame_layout.visibility = android.view.View.GONE
        error_linear_layout.visibility = android.view.View.VISIBLE
    }

    override fun backPressed() = fragmentPresenter.backClick()
}