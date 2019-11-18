package com.mandarine.targetList.common

import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.mandarine.targetList.R

fun View.buildSnackbar(
    message: String,
    @ColorRes backgroundResId: Int
): Snackbar {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_INDEFINITE)
    val textView = snackbar.view.findViewById<TextView>(R.id.snackbar_text)
    textView.gravity = Gravity.CENTER_VERTICAL
    textView.maxLines = 2
    snackbar.view.setBackgroundColor(ContextCompat.getColor(context, backgroundResId))
    snackbar.view.setOnTouchListener { _, _ ->
        snackbar.dismiss()
        true
    }
    return snackbar
}
