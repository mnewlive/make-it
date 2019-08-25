package com.mandarine.targetList.features.settings

import android.util.Log
import com.mandarine.targetList.R
import com.mandarine.targetList.features.settings.list.ImageTitleViewModel

class SettingsPresenter {

    fun getListItems(): List<Any> {
        return listOf(
            ImageTitleViewModel(R.drawable.ic_about, R.string.about),
            ImageTitleViewModel(R.drawable.ic_contact_us, R.string.contact_us)
        )
    }

    fun onListItemClick(item: ImageTitleViewModel?) {
        when (item?.textId ?: return) {
            R.string.about -> Log.d("some", "about")
            R.string.contact_us -> Log.d("some", "contact us")
        }
    }
}
