package com.mandarine.targetList.common.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.mandarine.targetList.R
import kotlinx.android.synthetic.main.view_sign_in_button.view.*

class SignInButtonView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_sign_in_button, this, true)
        initAttributes(context, attrs)
    }

    fun setText(textResId: Int) {
        titleView?.setText(textResId)
        invalidate()
    }

    private fun initAttributes(context: Context, attrs: AttributeSet) {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.SignInButtonView)
        try {
            attributes.getString(R.styleable.SignInButtonView_android_text)?.let { titleView?.text = it }
            titleView?.setTextColor(attributes.getColor(R.styleable.SignInButtonView_android_textColor, Color.BLACK))
            titleView?.isAllCaps = attributes.getBoolean(R.styleable.SignInButtonView_allCaps, false)
            val imageSrcId = attributes.getResourceId(R.styleable.SignInButtonView_imageSrc, -1)
            if (imageSrcId > 0) logoView?.setImageResource(imageSrcId)
        } finally {
            attributes.recycle()
        }
    }
}
