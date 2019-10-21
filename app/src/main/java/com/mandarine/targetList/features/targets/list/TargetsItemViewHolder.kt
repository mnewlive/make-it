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
import java.text.SimpleDateFormat

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
    private var dateView = itemView.findViewById<TextView>(R.id.dateView)

    init {
        itemView.setOnClickListener { reactOnClick() }
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
        val selectedDate = item.date
        val dateString = SimpleDateFormat("d MMMM, yyyy").format(selectedDate)
        dateView.text = dateString
    }

    private fun reactOnClick() {
        if (adapterPosition > RecyclerView.NO_POSITION) listener?.onListItemClick(
            adapterPosition,
            ""
        )
    }
}
