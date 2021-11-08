package com.project.dictionary.view.descriptionscreen

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.project.dictionary.R
import com.project.dictionary.utils.network.isOnline
import com.project.dictionary.utils.ui.AlertDialogFragment
import com.project.dictionary.view.BackButtonListener
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_description.*
import org.koin.android.ext.android.getKoin
import ru.terrakok.cicerone.Router

class DescriptionFragment: Fragment(), BackButtonListener {

    private val router: Router = getKoin().get()

    companion object {

        private const val DIALOG_FRAGMENT_TAG = "com.project.dictionary.view.descriptionscreen.dialogfragmenttag"
        private const val WORD_EXTRA = "com.project.dictionary.view.descriptionscreen.wordextra"
        private const val DESCRIPTION_EXTRA = "com.project.dictionary.view.descriptionscreen.descriptionextra"
        private const val URL_EXTRA = "com.project.dictionary.view.descriptionscreen.urlextra"

        fun newInstance(word: String, description: String, url: String?) = DescriptionFragment().apply {
            arguments = Bundle().apply {
                putString(WORD_EXTRA, word)
                putString(DESCRIPTION_EXTRA, description)
                putString(URL_EXTRA, url)
                println("URL = $url")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = View.inflate(context, R.layout.fragment_description, null)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // setHasOptionsMenu(true) makes is possible to handle clicks on menu items
        setHasOptionsMenu(true)
        setActionbarHomeButtonEnable()
        description_screen_swipe_refresh_layout.setOnRefreshListener{ startLoadingOrShowError() }
        setData()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                println("Back pressed!")
                backPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setActionbarHomeButtonEnable() {
        (requireActivity() as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setActionbarHomeButtonDisable(){
        (requireActivity() as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(false)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun setData() {
        description_header.text = arguments?.getString(WORD_EXTRA)
        description_textview.text = arguments?.getString(DESCRIPTION_EXTRA)
        val imageLink = arguments?.getString(URL_EXTRA)
        if (imageLink.isNullOrBlank()) {
            stopRefreshAnimationIfNeeded()
            println("NO IMAGE")
        } else {
            //usePicassoToLoadPhoto(description_imageview, imageLink)
            useGlideToLoadPhoto(description_imageview, imageLink)
        }
    }

    private fun startLoadingOrShowError() {
        if (context?.let { isOnline(it) } == true) {
            setData()
        } else {
            AlertDialogFragment.newInstance(
                getString(R.string.dialog_title_device_is_offline),
                getString(R.string.dialog_message_device_is_offline)
            ).show(
                childFragmentManager,
                DIALOG_FRAGMENT_TAG
            )
            stopRefreshAnimationIfNeeded()
        }
    }

    private fun stopRefreshAnimationIfNeeded() {
        if (description_screen_swipe_refresh_layout.isRefreshing) {
            description_screen_swipe_refresh_layout.isRefreshing = false
        }
    }

    private fun usePicassoToLoadPhoto(imageView: ImageView, imageLink: String) {
        Picasso.with(context).load("https:$imageLink")
            .placeholder(R.drawable.ic_no_photo_vector).fit().centerCrop()
            .error(R.drawable.ic_load_error_vector)
            .into(imageView, object : Callback {
                override fun onSuccess() {
                    stopRefreshAnimationIfNeeded()
                }

                override fun onError() {
                    stopRefreshAnimationIfNeeded()
                }
            })
    }

    private fun useGlideToLoadPhoto(imageView: ImageView, imageLink: String) {
        Glide.with(imageView)
            .load("https:$imageLink")
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    stopRefreshAnimationIfNeeded()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    stopRefreshAnimationIfNeeded()
                    return false
                }
            })
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_no_photo_vector)
                    .error(R.drawable.ic_load_error_vector)
                    .centerCrop()
            )
            .into(imageView)
    }

    override fun backPressed(): Boolean {
        setActionbarHomeButtonDisable()
        router.exit()
        return true
    }

}