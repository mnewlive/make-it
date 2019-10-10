package com.mandarine.targetList.common

import androidx.recyclerview.widget.RecyclerView

abstract class AbstractListAdapter <T>(initialData: List<T> = emptyList())  :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var _data: MutableList<T> = initialData.toMutableList()
    var data: List<T>
        get() = _data
        set(value) {
            _data = value.toMutableList()
            notifyDataSetChanged()
        }

    fun getItem(position: Int) = _data.getOrNull(position)

    protected fun isLastPosition(position: Int) = position == itemCount - 1

    override fun getItemCount(): Int = _data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindHolder(holder, position, _data[position])
    }

    abstract fun onBindHolder(holder: RecyclerView.ViewHolder, position: Int, item: T)
}
