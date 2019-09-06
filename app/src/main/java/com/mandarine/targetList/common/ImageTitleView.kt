package com.mandarine.targetList.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.mandarine.targetList.R
import kotlinx.android.synthetic.main.view_image_title.view.*

class ImageTitleView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_image_title, this)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ImageTitleView)
        try {
            val titleText = attributes.getString(R.styleable.ImageTitleView_titleText)
            titleText?.let { setTitleText(titleText) }
            val imageSrcId = attributes.getResourceId(R.styleable.ImageTitleView_imageSrc, -1)
            if (imageSrcId > 0) setImageResource(imageSrcId)
        } finally {
            attributes.recycle()
        }
    }

    fun setTitleText(value: String) {
        titleView?.text = value
    }

    fun setTitleText(value: Int) {
        titleView?.text = context.getString(value)
    }

    fun setTitleTextColor(value: Int) {
        titleView?.setTextColor(value)
    }

    fun setImageResource(value: Int) = iconView?.setImageResource(value)
}
