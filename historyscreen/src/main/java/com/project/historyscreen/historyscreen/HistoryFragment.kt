package com.project.historyscreen.historyscreen

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.core.BackButtonListener
import com.project.core.base.BaseFragment
import com.project.dictionary.di.modules.injectDependencies
import com.project.historyscreen.R
import com.project.model.data.AppState
import com.project.model.data.DataModel
import kotlinx.android.synthetic.main.fragment_history.*
import org.koin.android.scope.currentScope


class HistoryFragment : BaseFragment<AppState>(), BackButtonListener {

    companion object {
        fun newInstance() = HistoryFragment()
        private const val BOTTOM_SHEET_FRAGMENT_DIALOG_TAG = "com.project.dictionary.view.historyscreen.bottomsheetfragmentgialog"
    }

    override val model: HistoryViewModel by lazy {
        injectDependencies()
        currentScope.get()
    }

    private val observer = Observer<AppState> { renderData(it)  }
    private var adapter: HistoryAdapter? = null
    private val onListItemClickListener: HistoryAdapter.OnListItemClickListener =
        object : HistoryAdapter.OnListItemClickListener {
            override fun onItemClick(data: DataModel) {
                model.wordClicked(data)
            }
        }


    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View {
        val v: View = inflater.inflate(R.layout.fragment_history, parent, false)
        setHasOptionsMenu(true)
        return v
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.history_menu_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search_menu_item -> {
                println("Search pressed!")
                openSearchDialogFragment()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun openSearchDialogFragment(){
        val searchDialogFragment = SearchDialogFragment.newInstance()
        searchDialogFragment.setOnSearchClickListener(object : SearchDialogFragment.OnSearchClickListener {
            override fun onClick(searchWord: String) {
                model.getData(searchWord, false)
            }
        })
        searchDialogFragment.show(childFragmentManager, BOTTOM_SHEET_FRAGMENT_DIALOG_TAG)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("history fragment viewmodel: $model ")
        model.subscribe().observe(viewLifecycleOwner, observer)
    }

    override fun onResume() {
        super.onResume()
        // Ask for data from local repo (isOnline = false)
        model.getData("", false)
    }

    override fun backPressed(): Boolean = model.backPressed()

    override fun setDataToAdapter(data: List<DataModel>) {
        if (adapter == null) {
            history_fragment_recyclerview.layoutManager = LinearLayoutManager(context)
            history_fragment_recyclerview.adapter = HistoryAdapter(onListItemClickListener, data)
        } else {
            adapter!!.setData(data)
        }
    }

}