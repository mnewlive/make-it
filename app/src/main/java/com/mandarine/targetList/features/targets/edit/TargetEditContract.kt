package com.mandarine.targetList.features.targets.edit

interface TargetEditContract {
    fun editTarget(targetGuid: String)
    fun deleteTarget(targetGuid: String)
    fun updateViewsContent(name: String?, description: String?)

}
