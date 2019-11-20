package com.mandarine.targetList.model

data class Goal(
    val guid: String = "",
    val name: String = "",
    val description: String = "",
    val deadline: Long = 0L,
    var priority: Int = 0
)
