package com.matrix_maeny.taskmanager.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.matrix_maeny.taskmanager.R
import com.matrix_maeny.taskmanager.data.TaskProgressDialog
import com.matrix_maeny.taskmanager.databinding.ActivityLoginBinding
import com.matrix_maeny.taskmanager.listeners.AuthListener
import com.matrix_maeny.taskmanager.repositories.NetworkingRepository
import com.matrix_maeny.taskmanager.ui.home.TaskManagerActivity
import com.matrix_maeny.taskmanager.utils.getAuthenticationRepository
import com.matrix_maeny.taskmanager.utils.toast
import com.matrix_maeny.taskmanager.viewmodels.LoginViewModel

class LoginActivity : AppCompatActivity(), AuthListener {

    lateinit var dataBinding: ActivityLoginBinding
    lateinit var viewModel: LoginViewModel
    lateinit var taskProgressDialog: TaskProgressDialog



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        )

        dataBinding = DataBindingUtil.setContentView(this@LoginActivity, R.layout.activity_login)
        viewModel = ViewModelProviders.of(this)[LoginViewModel::class.java]
        viewModel.authListener = this
        dataBinding.viewModel = viewModel


        viewModel.authenticationRepository = getAuthenticationRepository(application)
        viewModel.networkingRepository = NetworkingRepository(application)

        taskProgressDialog = TaskProgressDialog(this, "Logging in", "Please wait...")


        setupObservers()

    }

    private fun setupObservers() {
        viewModel.getUserData()?.observe(this) {
            taskProgressDialog.dismiss()

            if (it != null) {
                startActivity(Intent(this, TaskManagerActivity::class.java))
                ActivityCompat.finishAffinity(this)
            }
        }

    }


    override fun loginStarted() {
        taskProgressDialog.show()
    }


    override fun onFailure(message: String) {
        taskProgressDialog.dismiss()
        toast(message)
    }

    override fun gotoActivity() {
        startActivity(Intent(this, SignUpActivity::class.java))
    }



}