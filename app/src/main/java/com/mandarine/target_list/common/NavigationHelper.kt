package com.mandarine.target_list.common

import android.app.Activity
import android.app.Fragment
import com.mandarine.target_list.R

fun Activity?.replaceFragment(fragment: Fragment): Boolean {
    if (this == null) return false
    try {
        fragmentManager?.beginTransaction()?.replace(R.id.container, fragment, fragment.createTagName())?.commit()
    } catch (ignored: IllegalStateException) { return false }
    return true
}

fun android.app.Fragment?.createTagName(): String? = this?.javaClass?.name