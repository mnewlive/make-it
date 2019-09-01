package com.mandarine.targetList.features.settings

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.auth.AuthUI
import com.mandarine.targetList.R
import com.mandarine.targetList.common.showLogoutDialog
import com.mandarine.targetList.features.settings.list.ImageTitleViewModel
import com.mandarine.targetList.interfaces.ListItemClickListener
import kotlinx.android.synthetic.main.fragment_settings_list.*

class SettingsListFragment : Fragment(), ListItemClickListener,
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

    // https://github.com/firebase/FirebaseUI-Android/blob/master/auth/README.md#sign-out
    override fun logOut() {
        activity?.showLogoutDialog(positiveListener = this)
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        AuthUI.getInstance().signOut(requireContext())
    }

    override fun onListItemClick(itemIndex: Int, itemCode: String) {
        presenter.onListItemClick(adapter.getItem(itemIndex) as? ImageTitleViewModel)
    }

    private fun setupViews() {
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        adapter.data = presenter.getListItems()
        recyclerView?.adapter = adapter
        nameView?.text = presenter.displayName()
        emailView?.text = presenter.displayEmail()
    }
}
