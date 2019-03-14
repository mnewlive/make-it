package com.mandarine.target_list

interface MainActivityViewContract {
    fun cancelSignIn()
    fun signOut(): Boolean
    fun addTarget()
}