package com.mandarine.targetList.features.targets.list

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mandarine.targetList.R
import com.mandarine.targetList.common.inflateListItemView
import com.mandarine.targetList.interfaces.ListItemClickListener
import com.mandarine.targetList.model.Target

class TargetsItemViewHolder(
    parent: ViewGroup,
    private val listener: ListItemClickListener?
) : RecyclerView.ViewHolder(
    parent.inflateListItemView(
        R.layout.list_item_targets
    )
) {

    private var titleView = itemView.findViewById<TextView>(R.id.titleView)
    private var descriptionView = itemView.findViewById<TextView>(R.id.descriptionView)
    private var priorityView = itemView.findViewById<View>(R.id.priorityView)

    init {
        itemView.setOnClickListener { reactOnClick() }
    }

    fun reactOnClick() {
        if (adapterPosition > RecyclerView.NO_POSITION) listener?.onListItemClick(
            adapterPosition,
            ""
        )
    }

    fun bind(item: Target) {
        titleView.text = item.name
        descriptionView.text = item.description
        priorityView.setBackgroundColor(
            ContextCompat.getColor(
                itemView.context,
                R.color.colorPrimary
            )
        )
    }
}
