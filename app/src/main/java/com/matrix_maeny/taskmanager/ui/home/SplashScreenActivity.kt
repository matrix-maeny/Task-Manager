package com.matrix_maeny.taskmanager.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.matrix_maeny.taskmanager.R
import com.matrix_maeny.taskmanager.repositories.AuthenticationRepository
import com.matrix_maeny.taskmanager.ui.auth.LoginActivity
import com.matrix_maeny.taskmanager.utils.getAuthenticationRepository
import com.matrix_maeny.taskmanager.viewmodels.SignUpViewModel
import com.matrix_maeny.taskmanager.viewmodels.SplashScreenViewModel

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        )


        val viewModel = ViewModelProvider(this)[SplashScreenViewModel::class.java]
        viewModel.authenticationRepository = getAuthenticationRepository(application)

        Handler().postDelayed({
            viewModel.getUserData()?.observe(this) {

                if (it != null) {
                    startActivity(
                        Intent(
                            this@SplashScreenActivity,
                            TaskManagerActivity::class.java
                        )
                    )
                } else {


                    startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
                }
                ActivityCompat.finishAffinity(this)

            }
        }, 2000)


    }
}