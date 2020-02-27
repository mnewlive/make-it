package com.mandarine.targetList.model

import com.google.firebase.database.PropertyName

data class Goal(
    val guid: String = "",
    val name: String = "",
    val description: String = "",
    val deadline: Long = 0L,
    var priority: Int = 0,
    @get:PropertyName("isComplete") var isComplete: Boolean = false
)
