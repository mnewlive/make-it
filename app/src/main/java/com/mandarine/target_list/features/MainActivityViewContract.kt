package com.mandarine.target_list.features

interface MainActivityViewContract {
    fun cancelSignIn()
    fun signOut(): Boolean
    fun addTarget()
}