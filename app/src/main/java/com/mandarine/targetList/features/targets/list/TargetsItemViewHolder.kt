package com.mandarine.targetList.features.targets.list

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mandarine.targetList.R
import com.mandarine.targetList.common.tools.inflateListItemView
import com.mandarine.targetList.interfaces.ListItemClickListener
import com.mandarine.targetList.model.Goal
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
    private var guid = ""

    init {
        itemView.setOnClickListener { reactOnClick() }
    }

    fun bind(item: Goal) {
        titleView.text = item.name
        descriptionView.text = item.description
        priorityView.setBackgroundColor(
            ContextCompat.getColor(
                itemView.context,
                setColorForPriority(item.priority)
            )
        )
        val selectedDate = item.deadline
        val dateString = SimpleDateFormat("d MMMM, yyyy").format(selectedDate)
        dateView.text = dateString
        guid = item.guid
    }

    private fun setColorForPriority(priorityPosition: Int): Int {
        return when (priorityPosition) {
            0 -> R.color.colorAccent
            1 -> R.color.yellow
            else -> R.color.colorPrimary
        }
    }

    private fun reactOnClick() {
        if (adapterPosition > RecyclerView.NO_POSITION) listener?.onListItemClick(
            itemIndex = adapterPosition,
            itemCode = guid
        )
    }
}
