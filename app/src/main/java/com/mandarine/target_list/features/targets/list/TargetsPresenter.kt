package com.mandarine.target_list.features.targets.list

import com.mandarine.target_list.interfaces.SelectTargetViewContract
import com.mandarine.target_list.model.Target

class TargetsPresenter(private val contract: SelectTargetViewContract) {

    fun onListItemClick(item: Target) {
        contract.showTarget(item.guid)
    }
}