package com.matrix_maeny.taskmanager.viewmodels

import androidx.lifecycle.ViewModel
import com.matrix_maeny.taskmanager.repositories.AuthenticationRepository

class SplashScreenViewModel:ViewModel() {

    var authenticationRepository: AuthenticationRepository? = null

    fun getUserData() = authenticationRepository?.getFirebaseUserMLD()


}