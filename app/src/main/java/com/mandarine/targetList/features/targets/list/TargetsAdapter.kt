package com.mandarine.targetList.features.targets.list

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.mandarine.targetList.R
import com.mandarine.targetList.interfaces.ListItemClickListener
import com.mandarine.targetList.model.Target

class TargetsAdapter(
    private val data: MutableList<Target>,
    private val clickListener: ListItemClickListener
) : RecyclerView.Adapter<TargetsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_targets, parent, false)
        return ViewHolder(view, clickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun getItem(position: Int) = data[position]

    // TODO: Rename on smth like TargetsItemViewHolder
    inner class ViewHolder(
        view: View,
        private val listener: ListItemClickListener?
    ) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private var titleView = itemView.findViewById<TextView>(R.id.titleView)
        private var descriptionView = itemView.findViewById<TextView>(R.id.descriptionView)
        private var priorityView = itemView.findViewById<View>(R.id.priorityView)

        init {
            titleView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (adapterPosition > RecyclerView.NO_POSITION) listener?.onListItemClick(
                adapterPosition,
                ""
            )
        }

        fun bind(item: Target) {
            titleView.text = item.name
            descriptionView.text = item.description
            priorityView.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.colorPrimary))
        }
    }
}
