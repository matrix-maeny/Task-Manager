package com.matrix_maeny.taskmanager.repositories

import android.app.Application
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.matrix_maeny.taskmanager.R
import com.matrix_maeny.taskmanager.data.SharedData
import com.matrix_maeny.taskmanager.data.User
import com.matrix_maeny.taskmanager.utils.toast

class AuthenticationRepository(private val application: Application) {

    private val firebaseUserMLD: MutableLiveData<FirebaseUser?> = MutableLiveData()
    private val isLoggedInMLD: MutableLiveData<Boolean> = MutableLiveData()

    private val gmailStartActivityIntent: MutableLiveData<Intent> = MutableLiveData()
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()


    init {
        firebaseUserMLD.postValue(firebaseAuth.currentUser)
    }

    fun registerAccountWithEmail(fullName: String, email: String, password: String) {

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            isLoggedInMLD.postValue(it.isSuccessful)

            if (it.isSuccessful) {

                // write realtime database storage here for user authentications.......
                saveUserToDB(fullName, email, password)
            } else {
                it.exception?.message?.let { it1 -> application.toast(it1) }
                firebaseUserMLD.postValue(null)

            }

        }.addOnFailureListener {
            isLoggedInMLD.postValue(false)
            it.message?.let { it1 -> application.toast(it1) }
        }

    }

    private fun saveUserToDB(fullName: String, email: String, password: String) {

        (if (firebaseAuth.currentUser != null) firebaseAuth.currentUser!!.uid else null)?.let { Uid ->
            firebaseDatabase.reference.child("users").child(Uid)
                .setValue(User(fullName, email, password))
                .addOnCompleteListener {

                    isLoggedInMLD.postValue(it.isSuccessful)

                    if (it.isSuccessful) {
                        firebaseUserMLD.postValue(firebaseAuth.currentUser)
//                        application.toast("Account created successfully")

                    } else {
                        it.exception?.message?.let { it1 -> application.toast(it1) }
                        firebaseUserMLD.postValue(null)
                    }

                }.addOnFailureListener {
                    firebaseUserMLD.postValue(null)
//                    isLoggedInMLD.postValue(false)
                    it.message?.let { it1 -> application.toast(it1) }

                }
        }

    }

    fun loginAccount(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            isLoggedInMLD.postValue(it.isSuccessful)

            if (it.isSuccessful) {
                firebaseUserMLD.postValue(firebaseAuth.currentUser)
            } else {
                firebaseUserMLD.postValue(null)
                it.exception?.message?.let { it1 -> application.toast(it1) }
            }
        }.addOnFailureListener {
            isLoggedInMLD.postValue(false)
            firebaseUserMLD.postValue(null)
            it.message?.let { it1 -> application.toast(it1) }
        }
    }

    fun logOut() {
        SharedData.tasks.taskList?.clear()
        Firebase.auth.signOut()
        isLoggedInMLD.postValue(false)
    }

    fun getFirebaseUserMLD() = firebaseUserMLD
    fun getIsLoggedInMLD() = isLoggedInMLD
    fun getGmailStartActivityIntent() = gmailStartActivityIntent


}