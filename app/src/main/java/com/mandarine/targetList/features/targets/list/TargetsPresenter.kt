package com.mandarine.targetList.features.targets.list

import com.mandarine.targetList.interfaces.SelectTargetViewContract
import com.mandarine.targetList.model.Target

class TargetsPresenter(private val contract: SelectTargetViewContract) {

    fun onListItemClick(item: Target) {
        contract.showTarget(item.guid)
    }
}