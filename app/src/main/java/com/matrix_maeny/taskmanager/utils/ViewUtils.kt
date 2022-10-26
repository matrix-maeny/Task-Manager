package com.matrix_maeny.taskmanager.utils

import android.app.Application
import android.content.Context
import android.widget.Toast
import com.matrix_maeny.taskmanager.repositories.AuthenticationRepository
import com.matrix_maeny.taskmanager.repositories.TaskingRepository

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.getAuthenticationRepository(application: Application) =
    AuthenticationRepository(application)

fun Context.getTaskingRepository(application: Application) =
    TaskingRepository(application)


