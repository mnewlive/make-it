package com.mandarine.targetList.features.root

interface MainActivityViewContract {
    fun cancelSignIn()
    fun signOut(): Boolean
    fun addTarget()
    fun showListOfTarget()
}
