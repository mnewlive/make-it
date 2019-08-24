package com.mandarine.targetList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun ViewGroup.inflateListItemView(resId: Int): View =
    LayoutInflater.from(context).inflate(resId, this, false)
