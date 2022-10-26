package com.matrix_maeny.taskmanager.ui.auth

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.matrix_maeny.taskmanager.R
import com.matrix_maeny.taskmanager.data.TaskProgressDialog
import com.matrix_maeny.taskmanager.databinding.ActivitySignUpBinding
import com.matrix_maeny.taskmanager.listeners.AuthListener
import com.matrix_maeny.taskmanager.repositories.NetworkingRepository
import com.matrix_maeny.taskmanager.ui.home.TaskManagerActivity
import com.matrix_maeny.taskmanager.utils.getAuthenticationRepository
import com.matrix_maeny.taskmanager.utils.toast
import com.matrix_maeny.taskmanager.viewmodels.SignUpViewModel

class SignUpActivity : AppCompatActivity(), AuthListener {

    lateinit var dataBinding: ActivitySignUpBinding
    lateinit var taskProgressDialog: TaskProgressDialog



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        )

        dataBinding = DataBindingUtil.setContentView(this@SignUpActivity, R.layout.activity_sign_up)
        val viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]

        dataBinding.viewModel = viewModel
        viewModel.authListener = this
        viewModel.networkingRepository = NetworkingRepository(application)


        viewModel.authenticationRepository = getAuthenticationRepository(application)

        taskProgressDialog = TaskProgressDialog(this,"Signing up","Please wait...")


        viewModel.getUserData()?.observe(this) {

            taskProgressDialog.dismiss()

            if (it != null) {
                startActivity(Intent(this@SignUpActivity, TaskManagerActivity::class.java))
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
        taskProgressDialog.dismiss()
        startActivity(Intent(this, LoginActivity::class.java))
        ActivityCompat.finishAffinity(this)

    }

}