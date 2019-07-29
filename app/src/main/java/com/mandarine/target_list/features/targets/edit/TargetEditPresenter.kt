package com.mandarine.target_list.features.targets.edit

import com.mandarine.target_list.R

class TargetEditPresenter(private val contract: TargetEditContract) {

    fun onViewClick(id: Int, targetGuid: String) {
        when (id) {
            R.id.addNote -> contract.editTarget(targetGuid)
            R.id.deleteButton -> contract.deleteTarget(targetGuid)
        }
    }
}