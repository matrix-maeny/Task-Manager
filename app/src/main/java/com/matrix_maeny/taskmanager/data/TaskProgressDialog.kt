package com.matrix_maeny.taskmanager.data

import android.app.ProgressDialog
import android.content.Context
import android.widget.ProgressBar


class TaskProgressDialog(context: Context,title:String,message:String){


    private val progressDialog: ProgressDialog = ProgressDialog(context)

    init {

        progressDialog.setTitle(title)
        progressDialog.setMessage(message)
        progressDialog.setCancelable(false)
    }

    fun setMessages(title: String,message: String){
        progressDialog.setTitle(title)
        progressDialog.setMessage(message)
    }

    fun show(){
        progressDialog.show()
    }

    fun dismiss(){
        progressDialog.dismiss()
    }

}