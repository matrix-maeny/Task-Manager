package com.matrix_maeny.taskmanager.viewmodels

import android.view.View
import androidx.lifecycle.ViewModel
import com.matrix_maeny.taskmanager.repositories.AuthenticationRepository
import com.matrix_maeny.taskmanager.listeners.AuthListener
import com.matrix_maeny.taskmanager.repositories.NetworkingRepository

class SignUpViewModel : ViewModel() {

    var fullName: String? = null // fot the full name
    var email: String? = null       // for email
    var password: String? = null    // for password
    var confirmPassword: String? = null // variable to confirm password
    var authListener: AuthListener? = null // a sign up listener
    var authenticationRepository: AuthenticationRepository? = null

    var networkingRepository: NetworkingRepository? = null


    fun onSignUpBtnClick(view: View) {
        authListener?.loginStarted() // show process is started
        if (fullName.isNullOrEmpty() || email.isNullOrEmpty() || password.isNullOrEmpty() || confirmPassword.isNullOrEmpty()) {

            authListener?.onFailure("Please enter valid details")
            return
        }
        if (password.equals(confirmPassword)) {
            if (networkingRepository?.isNetworkAvailable == true) {
                authenticationRepository?.registerAccountWithEmail(fullName!!, email!!, password!!)
            }else authListener?.onFailure("Please connect to a network")
        }
        else authListener?.onFailure("Password doesn't matched")





    }

    fun onSignInClick(view: View) {
        authListener?.gotoActivity()
    }

    fun getUserData() = authenticationRepository?.getFirebaseUserMLD()
    fun getLoggedStatus() = authenticationRepository?.getIsLoggedInMLD()

}