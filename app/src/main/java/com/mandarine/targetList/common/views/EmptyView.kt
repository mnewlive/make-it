package com.mandarine.targetList.common.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.mandarine.targetList.R
import com.mandarine.targetList.common.tools.setVisible
import kotlinx.android.synthetic.main.view_empty.view.*

class EmptyView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_empty, this, true)
        initAttributes(context, attrs)
    }

    fun setIconResource(resId: Int) {
        iconView?.setVisible(resId != 0)
        iconView?.setImageResource(resId)
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
            val iconResId = attributes.getResourceId(R.styleable.EmptyView_iconSrc, 0)
            val titleText = attributes.getString(R.styleable.EmptyView_titleText)
            val subTitleText = attributes.getString(R.styleable.EmptyView_subTitleText)

            setIconResource(iconResId)
            setTitleText(titleText ?: "")
            setSubTitleText(subTitleText ?: "")
        } finally {
            attributes.recycle()
        }
    }
}
