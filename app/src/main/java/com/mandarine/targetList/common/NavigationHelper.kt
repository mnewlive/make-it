package com.mandarine.targetList.common

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import com.mandarine.targetList.R

/**
 * Replace fragment in container if backStackEntryCount > 0
 *
 * @receiver fragment activity
 */
fun FragmentActivity?.replaceFragment(fragment: Fragment): Boolean {
    if (this == null) return false
    try {
        supportFragmentManager?.beginTransaction()?.replace(
            R.id.container, fragment,
            fragment.createTagName()
        )?.commit()
    } catch (ignored: IllegalStateException) {
        return false
    }
    return true
}

/**
 * Add fragment in back stack
 *
 * @receiver fragment activity
 */
fun FragmentActivity?.addFragment(fragment: Fragment) {
    if (this == null) return
    try {
        supportFragmentManager?.beginTransaction()?.replace(
            R.id.container, fragment,
            fragment.createTagName()
        )?.addToBackStack(null)?.commit()
    } catch (ignored: IllegalStateException) {
    }
}

/**
 * Finish fragment
 *
 * @receiver fragment activity
 */
fun FragmentActivity.finishFragment() {
    try {
        supportFragmentManager?.popBackStack()
    } catch (ignored: IllegalStateException) {
    } catch (e: Exception) {
    }
}

/**
 * Get current fragment in container
 *
 * @receiver app compat activity
 * @return fragment object which is in the container
 */
fun AppCompatActivity.currentFragmentInContainer(): Fragment? {
    try {
        return supportFragmentManager?.findFragmentById(R.id.container)
    } catch (ignored: IllegalStateException) {
    } catch (e: Exception) {
    }
    return null
}

/**
 * Create tag name for fragment
 *
 * @receiver fragment
 * @return fragment name
 */
private fun Fragment?.createTagName(): String? = this?.javaClass?.name
