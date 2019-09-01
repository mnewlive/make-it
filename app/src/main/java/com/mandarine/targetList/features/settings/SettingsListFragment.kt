package com.mandarine.targetList.features.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mandarine.targetList.R
import com.mandarine.targetList.features.settings.list.ImageTitleViewModel
import com.mandarine.targetList.interfaces.ListItemClickListener
import kotlinx.android.synthetic.main.fragment_settings_list.*

class SettingsListFragment : Fragment(), ListItemClickListener {

    private val adapter = SettingsListAdapter(clickListener = this)
    private val presenter = SettingsPresenter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        adapter.data = presenter.getListItems()
        recyclerView?.adapter = adapter
        nameView?.text = presenter.displayName()
        emailView?.text = presenter.displayEmail()
    }

    override fun onListItemClick(itemIndex: Int, itemCode: String) {
        presenter.onListItemClick(adapter.getItem(itemIndex) as? ImageTitleViewModel)
    }
}
