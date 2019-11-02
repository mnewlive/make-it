package com.mandarine.targetList.model

data class Target(
    val guid: String = "",
    val name: String = "",
    val description: String = "",
    val date: Long = 0L,
    var priority: Int = 0
)
