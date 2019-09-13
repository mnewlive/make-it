package com.mandarine.targetList.common

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.mandarine.targetList.model.Target

fun List<Target>.indexIsInBounds(index: Int) = index > -1 && index < size

fun Context.getDrawableCompat(id: Int): Drawable = ContextCompat.getDrawable(this, id)
    ?: ColorDrawable(Color.TRANSPARENT)
