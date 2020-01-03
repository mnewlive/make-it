package com.mandarine.targetList.features

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.mandarine.targetList.R
import com.mandarine.targetList.common.buildSnackbar
import com.mandarine.targetList.widget.BaseFragment
import kotlinx.android.synthetic.main.fragment_tabs.*

class TabsFragment : BaseFragment() {

    private var snackbar: Snackbar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tabs, container, false)
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected) {
            snackbar?.dismiss()
        } else {
            snackbar = activity?.coordinatorLayout?.buildSnackbar(
                message = getString(R.string.warning_no_internet_connections),
                backgroundResId = android.R.color.holo_orange_dark
            )
            snackbar?.show()
        }
    }

    private fun setupViews() {
        contentViewPager.adapter = MyPagerAdapter(activity!!.supportFragmentManager)
        tabLayout.setupWithViewPager(contentViewPager)

        fab?.setOnClickListener {
            findNavController().navigate(TabsFragmentDirections.nextAction(""))
        }
    }
}
