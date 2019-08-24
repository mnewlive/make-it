package com.mandarine.targetList.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.mandarine.targetList.R
import kotlinx.android.synthetic.main.view_empty.view.*

class EmptyView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_empty, this, true)
        initAttributes(context, attrs)
    }

    fun setTitleText(textId: Int) {
        setTitleText(context.getString(textId))
    }

    fun setTitleText(text: String) {
        titleView?.text = text
    }

    fun setSubTitleText(text: String) {
        subTitleView?.text = text
    }

    private fun initAttributes(context: Context, attrs: AttributeSet) {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.EmptyView)
        try {
            val titleText = attributes.getString(R.styleable.EmptyView_titleText)
            val subTitleText = attributes.getString(R.styleable.EmptyView_subTitleText)

            setTitleText(titleText ?: "")
            setSubTitleText(subTitleText ?: "")
        } finally {
            attributes.recycle()
        }
    }
}
