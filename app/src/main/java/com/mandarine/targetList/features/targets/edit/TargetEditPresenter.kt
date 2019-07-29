package com.mandarine.targetList.features.targets.edit

import com.mandarine.targetList.R

class TargetEditPresenter(private val contract: TargetEditContract) {

    fun onViewClick(id: Int, targetGuid: String) {
        when (id) {
            R.id.addNote -> contract.editTarget(targetGuid)
            R.id.deleteButton -> contract.deleteTarget(targetGuid)
        }
    }
}