package com.mandarine.targetList.features.settings

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.auth.AuthUI
import com.mandarine.targetList.R
import com.mandarine.targetList.common.dialogs.showLogoutDialog
import com.mandarine.targetList.common.runEmailApp
import com.mandarine.targetList.interfaces.ListItemClickListener
import com.mandarine.targetList.widget.BaseFragment
import kotlinx.android.synthetic.main.fragment_settings_list.*

class SettingsListFragment : BaseFragment(), ListItemClickListener,
    SettingsContract, DialogInterface.OnClickListener {

    private val adapter = SettingsListAdapter(clickListener = this)
    private val presenter = SettingsPresenter(contract = this)

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

    override fun logOut() {
        activity?.showLogoutDialog(positiveListener = this)
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        activity?.let {
            AuthUI.getInstance().signOut(it).addOnCompleteListener {
                findNavController().navigate(R.id.sign_in)
            }
        }
    }

    override fun onListItemClick(itemIndex: Int, itemCode: String) {
        presenter.onListItemClick(adapter.getItem(itemIndex))
    }

    override fun showEmailApp() {
        activity?.runEmailApp()
    }

    private fun setupViews() {
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        adapter.data = presenter.getListItems()
        recyclerView?.adapter = adapter
        nameView?.text = presenter.displayName()
        emailView?.text = presenter.displayEmail()
    }
}
