package com.mandarine.targetList.features.settings

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mandarine.targetList.features.settings.list.ImageTitleViewModel
import com.mandarine.targetList.features.targets.list.AbstractListAdapter
import com.mandarine.targetList.interfaces.ListItemClickListener

class SettingsListAdapter(private val clickListener: ListItemClickListener?) :
    AbstractListAdapter() {

    override fun onBindHolder(holder: RecyclerView.ViewHolder, position: Int, item: Any) =
        (holder as ImageTitleViewHolder).bind(
            (item as ImageTitleViewModel),
            isEndSeparatorPartial(position)
        )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ImageTitleViewHolder(parent, clickListener)

    private fun isEndSeparatorPartial(position: Int) = !isLastPosition(position)
}
