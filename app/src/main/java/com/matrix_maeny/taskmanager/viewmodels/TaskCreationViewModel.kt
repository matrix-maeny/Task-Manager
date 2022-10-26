package com.matrix_maeny.taskmanager.viewmodels

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.matrix_maeny.taskmanager.data.SharedData
import com.matrix_maeny.taskmanager.data.TaskModel
import com.matrix_maeny.taskmanager.listeners.TaskCreationListener
import com.matrix_maeny.taskmanager.repositories.NetworkingRepository
import com.matrix_maeny.taskmanager.repositories.TaskingRepository

class TaskCreationViewModel : ViewModel() {
    var taskName: String? = null
    var taskDetails: String? = null
    var taskDate: String = "Select date"

    var taskingRepository: TaskingRepository? = null
    var taskCreationListener: TaskCreationListener? = null

    var networkingRepository: NetworkingRepository? = null


    fun saveTaskToFirebase() {
        // save task to firebase
        if (networkingRepository?.isNetworkAvailable == false) {
            taskCreationListener?.uploadResult(
                "Please connect to a network"
            )
            return
        }

        if (taskName.isNullOrEmpty() || taskDetails.isNullOrEmpty() || taskDate == "Select date") {
            taskCreationListener?.uploadResult("")
            return
        }

//        taskCreationListener?.uploadResult(true)

        SharedData.tasks.taskList?.add(TaskModel(taskName!!.trim(), taskDate.trim(), taskDetails!!.trim(), false))
        taskingRepository?.uploadToFirebase()

    }

    fun onClickPicDate(view: View) {
        Log.i("MyDatePicker", "Entered")
        taskCreationListener?.openDatePickerDialog()
    }

    fun getSavedStatus() = taskingRepository?.isSaved
}