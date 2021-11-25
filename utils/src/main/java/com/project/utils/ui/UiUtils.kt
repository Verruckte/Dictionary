package com.project.utils.ui

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.*
import com.project.utils.R

fun getStubAlertDialog(context: Context): AlertDialog {
    return getAlertDialog(context, null, null)
}

fun getAlertDialog(context: Context, title: String?, message: String?): AlertDialog {
    val builder = AlertDialog.Builder(context)
    var finalTitle: String? = context.getString(R.string.dialog_title_stub)
    if (!title.isNullOrBlank()) {
        finalTitle = title
    }
    builder.setTitle(finalTitle)
    if (!message.isNullOrBlank()) {
        builder.setMessage(message)
    }
    builder.setCancelable(true)
    builder.setPositiveButton(R.string.dialog_button_cancel) { dialog, _ -> dialog.dismiss() }
    return builder.create()
}

fun Context.toast(message: Int) =
    Toast.makeText(this,  this.getResources().getString(message), Toast.LENGTH_SHORT).show()

fun recordInitialMarginForView(view: View) =
    Rect(view.marginLeft, view.marginTop, view.marginRight, view.marginBottom)

fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        println("isAttachedToWindow")
        ViewCompat.requestApplyInsets(this)

    } else {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}