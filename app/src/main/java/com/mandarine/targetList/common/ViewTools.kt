package com.mandarine.targetList.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Inflate list item view
 *
 * @param resId - the resource that need to inflate
 */
fun ViewGroup.inflateListItemView(resId: Int): View =
    LayoutInflater.from(context).inflate(resId, this, false)
