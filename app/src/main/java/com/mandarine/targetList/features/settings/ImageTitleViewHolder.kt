package com.mandarine.targetList.features.settings

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mandarine.targetList.R
import com.mandarine.targetList.common.views.ImageTitleView
import com.mandarine.targetList.common.tools.inflateListItemView
import com.mandarine.targetList.common.tools.setVisible
import com.mandarine.targetList.features.settings.list.ImageTitleViewModel
import com.mandarine.targetList.interfaces.ListItemClickListener

class ImageTitleViewHolder(parent: ViewGroup, private val listener: ListItemClickListener?) :
    RecyclerView.ViewHolder(parent.inflateListItemView(R.layout.list_item_image_title)),
    View.OnClickListener {

    private val imageTitleView = itemView.findViewById<ImageTitleView>(R.id.imageTitleView)
    private val bottomPartialDivider = itemView.findViewById<View>(R.id.bottomPartialDivider)

    init {
        imageTitleView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (adapterPosition > RecyclerView.NO_POSITION) listener?.onListItemClick(
            adapterPosition,
            ""
        )
    }

    fun bind(data: ImageTitleViewModel, isEndSeparatorPartial: Boolean) {
        imageTitleView.setImageResource(data.iconId)
        imageTitleView.setTitleText(data.textId)
        bottomPartialDivider.setVisible(isEndSeparatorPartial)
        imageTitleView.setTitleTextColor(
            ContextCompat.getColor(
                imageTitleView.context,
                data.textColorId
            )
        )
    }
}
