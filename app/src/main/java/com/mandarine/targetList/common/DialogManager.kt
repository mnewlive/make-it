package com.mandarine.targetList.common

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.fragment.app.FragmentActivity
import com.mandarine.targetList.R

fun FragmentActivity.showLogoutDialog(positiveListener: DialogInterface.OnClickListener) {
    try {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.log_out))
            .setMessage(getString(R.string.log_out_question))
            .setNegativeButton(getString(R.string.action_cancel), null)
            .setPositiveButton(getString(R.string.action_ok), positiveListener)
            .show()
    } catch (e: Exception) {
    }
}

fun FragmentActivity.showWarningDialog(positiveListener: DialogInterface.OnClickListener) {
    try {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.log_out))
            .setMessage(getString(R.string.log_out_question))
//            .setNegativeButton(getString(R.string.action_cancel), null)
//            .setPositiveButton(getString(R.string.action_ok), positiveListener)
            .show()
    } catch (e: Exception) {
    }
}
