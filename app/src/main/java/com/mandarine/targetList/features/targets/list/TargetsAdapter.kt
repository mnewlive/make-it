package com.mandarine.targetList.features.targets.list

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import com.mandarine.targetList.common.AbstractListAdapter
import com.mandarine.targetList.interfaces.ListItemClickListener
import com.mandarine.targetList.model.Goal

class TargetsAdapter(private val clickListener: ListItemClickListener?) : AbstractListAdapter<Goal>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        TargetsItemViewHolder(parent, clickListener)

    override fun onBindHolder(holder: RecyclerView.ViewHolder, position: Int, item: Goal) =
        (holder as TargetsItemViewHolder).bind(item)
}
