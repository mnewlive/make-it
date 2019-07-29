package com.mandarine.target_list.features.root

interface MainActivityViewContract {
    fun cancelSignIn()
    fun signOut(): Boolean
    fun addTarget()
    fun showListOfTarget()
}