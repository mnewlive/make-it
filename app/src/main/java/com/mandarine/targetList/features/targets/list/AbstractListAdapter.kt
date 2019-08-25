package com.mandarine.targetList.features.targets.list

import androidx.recyclerview.widget.RecyclerView

abstract class AbstractListAdapter(initialData: List<Any> = emptyList()) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var _data: MutableList<Any> = initialData.toMutableList()
    var data: List<Any>
        get() = _data
        set(value) {
            _data = value.toMutableList()
            notifyDataSetChanged()
        }
    val isEmpty: Boolean
        get() = _data.isEmpty()

    fun getItem(position: Int) = _data.getOrNull(position)

    protected fun isLastPosition(position: Int) = position == itemCount - 1

    override fun getItemCount(): Int = _data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindHolder(holder, position, _data[position])
    }

    abstract fun onBindHolder(holder: RecyclerView.ViewHolder, position: Int, item: Any)
}
