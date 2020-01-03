package com.mandarine.targetList.features

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mandarine.targetList.R
import com.mandarine.targetList.widget.BaseFragment
import kotlinx.android.synthetic.main.fragment_tabs.*

class TabsFragment : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MyPagerAdapter(activity!!.supportFragmentManager)
        viewpager_main.adapter = adapter
        tabs_main.setupWithViewPager(viewpager_main)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tabs, container, false)
    }
}
