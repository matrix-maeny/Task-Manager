package com.matrix_maeny.taskmanager.viewmodels

import android.view.View
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.matrix_maeny.taskmanager.data.SharedData
import com.matrix_maeny.taskmanager.repositories.AuthenticationRepository
import com.matrix_maeny.taskmanager.listeners.AuthListener
import com.matrix_maeny.taskmanager.repositories.NetworkingRepository

class LoginViewModel : ViewModel() {

    var email: String? = null// for email
    var password: String? = null// for password
    var authListener: AuthListener? = null // for auth listener
    var authenticationRepository: AuthenticationRepository? = null
    var networkingRepository: NetworkingRepository? = null


    fun onSignInClick(view: View) {

        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            authListener?.onFailure("Please enter valid details")

            return
        }
        authListener?.loginStarted()
        if (networkingRepository?.isNetworkAvailable == true) {
            authenticationRepository?.loginAccount(email!!, password!!)
        } else authListener?.onFailure("Please connect to a network")
    }





    fun onSignUpClick(view: View) {
        authListener?.gotoActivity()

    }


    fun getUserData() = authenticationRepository?.getFirebaseUserMLD()
    fun getGmailStartIntent() = authenticationRepository?.getGmailStartActivityIntent()


}