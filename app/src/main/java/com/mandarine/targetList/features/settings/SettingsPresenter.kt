package com.mandarine.targetList.features.settings

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.mandarine.targetList.R
import com.mandarine.targetList.features.settings.list.ImageTitleViewModel

class SettingsPresenter(private val contract: SettingsContract) {

    private var firebaseUser = FirebaseAuth.getInstance().currentUser

    fun getListItems(): List<Any> {
        return listOf(
            ImageTitleViewModel(R.drawable.ic_about, R.string.about),
            ImageTitleViewModel(R.drawable.ic_contact_us, R.string.contact_us),
            ImageTitleViewModel(iconId = R.drawable.ic_log_out, textId = R.string.log_out, textColorId = R.color.red)
        )
    }

    fun onListItemClick(item: ImageTitleViewModel?) {
        when (item?.textId ?: return) {
            R.string.about -> Log.d("some", "about")
            R.string.contact_us -> Log.d("some", "contact us")
            R.string.log_out -> contract.logOut()
        }
    }

    fun displayName(): String = firebaseUser?.displayName ?: "User name"

    fun displayEmail(): String = firebaseUser?.email ?: "User email"
}
