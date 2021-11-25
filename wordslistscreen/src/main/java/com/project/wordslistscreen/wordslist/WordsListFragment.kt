package com.project.wordslistscreen.wordslist

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.project.core.base.BaseFragment
import com.project.model.data.AppState
import com.project.model.data.DataModel
import com.project.wordslistscreen.R
import com.project.wordslistscreen.wordslist.adapter.WordsListRVAdapter
import kotlinx.android.synthetic.main.fragment_words_list.*
import org.koin.android.scope.currentScope

class WordsListFragment : BaseFragment<AppState>(), com.project.core.BackButtonListener {

    override lateinit var model: WordsListViewModel

    private val observer = Observer<AppState> { renderData(it)  }

    private var adapter: WordsListRVAdapter? = null
    private val onListItemClickListener: WordsListRVAdapter.OnListItemClickListener =
        object : WordsListRVAdapter.OnListItemClickListener {
            override fun onItemClick(data: DataModel) {
                model.wordClicked(data)
            }
        }
    private lateinit var splitInstallManager: SplitInstallManager


    companion object {
        fun newInstance() = WordsListFragment()
        private const val BOTTOM_SHEET_FRAGMENT_DIALOG_TAG = "12345"
        private const val HISTORY_ACTIVITY_FEATURE_NAME = "historyscreen"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    fun initViewModel(){
        model = currentScope.get()
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View {
        val v: View = inflater.inflate(R.layout.fragment_words_list, parent, false)
        setHasOptionsMenu(true)
        return v
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        println("model: $model ")
        model.subscribe().observe(viewLifecycleOwner, observer)

        search_fab.setOnClickListener {
            val searchDialogFragment = SearchDialogFragment.newInstance()
            searchDialogFragment.setOnSearchClickListener(object : SearchDialogFragment.OnSearchClickListener {
                override fun onClick(searchWord: String) {
                    if (isNetworkAvailable) {
                        model.getData(searchWord, isNetworkAvailable)
                    } else {
                        showNoInternetConnectionDialog()
                    }
                }
            })
            searchDialogFragment.show(childFragmentManager, BOTTOM_SHEET_FRAGMENT_DIALOG_TAG)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_history -> {
                val historyFragment = Class
                    .forName("com.lenatopoleva.dictionary.view.historyscreen.HistoryFragment")
                    .newInstance()

                splitInstallManager = SplitInstallManagerFactory.create(requireActivity())
                val request =
                    SplitInstallRequest
                        .newBuilder()
                        .addModule(HISTORY_ACTIVITY_FEATURE_NAME)
                        .build()

                splitInstallManager
                    .startInstall(request)
                    .addOnSuccessListener {
                        if (historyFragment != null) {
                            model.historyMenuItemClicked(historyFragment)
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            requireContext(),
                            "Couldn't download feature: " + it.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun backPressed(): Boolean = model.backPressed()

    override fun setDataToAdapter(data: List<DataModel>) {
        if (adapter == null) {
            words_list_recyclerview.layoutManager = LinearLayoutManager(context)
            words_list_recyclerview.adapter = WordsListRVAdapter(onListItemClickListener, data)
        } else {
            adapter!!.setData(data)
        }
    }

}