package com.mandarine.target_list.features.targets.edit

interface TargetEditContract {
    fun editTarget(targetGuid: String)
    fun deleteTarget(targetGuid: String)
}