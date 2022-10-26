package com.matrix_maeny.taskmanager.listeners


interface AuthListener {
    fun loginStarted()
    fun onFailure(message:String)
    fun gotoActivity()

}