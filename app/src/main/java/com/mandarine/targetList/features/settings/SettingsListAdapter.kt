package com.mandarine.targetList.features.settings

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mandarine.targetList.features.settings.list.ImageTitleViewModel
import com.mandarine.targetList.common.AbstractListAdapter
import com.mandarine.targetList.interfaces.ListItemClickListener

class SettingsListAdapter(private val clickListener: ListItemClickListener?) :
    AbstractListAdapter<ImageTitleViewModel>() {

    override fun onBindHolder(holder: RecyclerView.ViewHolder, position: Int, item: ImageTitleViewModel) =
        (holder as ImageTitleViewHolder).bind(
            item,
            isEndSeparatorPartial(position)
        )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ImageTitleViewHolder(parent, clickListener)

    private fun isEndSeparatorPartial(position: Int) = !isLastPosition(position)
}
