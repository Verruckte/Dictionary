package com.project.core.base

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.project.core.R
import com.project.core.viewmodel.BaseViewModel
import com.project.model.data.AppState
import com.project.model.data.DataModel
import com.project.utils.network.OnlineLiveData
import com.project.utils.ui.AlertDialogFragment
import com.project.utils.ui.getLastWord
import com.project.utils.ui.toast
import kotlinx.android.synthetic.main.loading_layout.*


abstract class BaseFragment<T : AppState> : Fragment() {

    abstract val model: BaseViewModel<T>

    protected var isNetworkAvailable: Boolean = false



    override fun onResume() {
        super.onResume()
        subscribeToNetworkChange()

        //Check SharedPreferencesDelegate:
        val word by getLastWord<String>()
        println("Last word = " + word)
    }

    protected fun renderData(appState: T) {
        when (appState) {
            is AppState.Success -> {
                showViewWorking()
                appState.data?.let {
                    if (it.isEmpty()) {
                        showAlertDialog(
                            getString(R.string.dialog_tittle_sorry),
                            getString(R.string.empty_server_response_on_success)
                        )
                    } else {
                        setDataToAdapter(it)
                    }
                } ?: showAlertDialog(
                    getString(R.string.dialog_tittle_sorry),
                    getString(R.string.empty_server_response_on_success)
                )
            }
            is AppState.Loading -> {
                showViewLoading()
                if (appState.progress != null) {
                    progress_bar_horizontal.visibility = View.VISIBLE
                    progress_bar_round.visibility = View.GONE
                    progress_bar_horizontal.progress = appState.progress!!
                } else {
                    progress_bar_horizontal?.visibility = View.GONE
                    progress_bar_round?.visibility = View.VISIBLE
                }
            }
            is AppState.Error -> {
                showViewWorking()
                showAlertDialog(getString(R.string.error_stub), appState.error.message)
            }
        }
    }

    protected fun showNoInternetConnectionDialog() {
        showAlertDialog(
            getString(R.string.dialog_title_device_is_offline),
            getString(R.string.dialog_message_device_is_offline)
        )
    }

    private fun showViewWorking() {
        loading_frame_layout?.visibility = View.GONE
    }

    private fun showViewLoading() {
        loading_frame_layout?.visibility = View.VISIBLE
    }

    protected fun showAlertDialog(title: String?, message: String?) {
        AlertDialogFragment.newInstance(title, message).show(requireFragmentManager(), DIALOG_FRAGMENT_TAG)
    }

    abstract fun setDataToAdapter(data: List<DataModel>)

    private fun isDialogNull(): Boolean {
        return requireFragmentManager().findFragmentByTag(DIALOG_FRAGMENT_TAG) == null
    }

    companion object {
        private const val DIALOG_FRAGMENT_TAG = "74a54328-5d62-46bf-ab6b-cbf5d8c79522"
    }

    private fun subscribeToNetworkChange() {
        OnlineLiveData(requireContext()).observe(
            viewLifecycleOwner,
            Observer<Boolean> {
                isNetworkAvailable = it
                println("NETWORK, available:$it")
                if (!isNetworkAvailable) {
                    requireContext().toast(R.string.dialog_message_device_is_offline)
                }
            })
    }
}