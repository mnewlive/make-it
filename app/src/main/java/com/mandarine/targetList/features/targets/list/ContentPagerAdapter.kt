package com.mandarine.targetList.features.targets.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.mandarine.targetList.R
import kotlinx.android.synthetic.main.rv_layout.view.*

class ContentPagerAdapter(
    val context: Context,
    private val targetsAdapter: TargetsAdapter
): PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.rv_layout, container, false)
        if (position == 0) {
            view.recyclerView.adapter = targetsAdapter
        }
        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = 2

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (position == 0) "Targets" else "Complete Tasks"
    }
}
