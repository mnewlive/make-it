package com.mandarine.target_list.features.targets.list

import android.widget.TextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mandarine.target_list.R
import com.mandarine.target_list.model.Target

class TargetsItemViewHolder(private val adsList: MutableList<Target>) :
    RecyclerView.Adapter<TargetsItemViewHolder.ViewHolder>() {

    override fun getItemCount(): Int {
        return adsList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_targets, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val target = adsList[position]
        holder.titleView.text = target.name
        holder.nameView.text = target.name
        holder.descriptionView.text = target.description
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var titleView = itemView.findViewById<TextView>(R.id.titleView)
        var nameView = itemView.findViewById<TextView>(R.id.nameView)
        var descriptionView = itemView.findViewById<TextView>(R.id.descriptionView)

        init {
            titleView = itemView.findViewById(R.id.titleView)
            nameView = itemView.findViewById(R.id.nameView)
            descriptionView = itemView.findViewById(R.id.descriptionView)
        }
    }
}