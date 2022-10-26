package com.matrix_maeny.taskmanager.viewmodels

import android.view.View
import androidx.lifecycle.ViewModel
import com.matrix_maeny.taskmanager.data.TaskModel
import com.matrix_maeny.taskmanager.listeners.TaskManagerListener
import com.matrix_maeny.taskmanager.repositories.AuthenticationRepository
import com.matrix_maeny.taskmanager.repositories.NetworkingRepository
import com.matrix_maeny.taskmanager.repositories.TaskingRepository
import com.matrix_maeny.taskmanager.utils.isRefresh

class TaskManagerViewModel : ViewModel() {

    var authenticationRepository: AuthenticationRepository? = null

    var taskingRepository:TaskingRepository? = null
    var networkingRepository: NetworkingRepository? = null

    var taskManagerListener:TaskManagerListener? = null
//    var emptyText:String = "Please connect to a network"




    fun logout(){
//        if (networkingRepository?.isNetworkAvailable == true) {
            authenticationRepository?.logOut()
//        }else taskManagerListener?.onFailure("No Internet connection")
    }

//    fun uploadToFirebase(taskList:ArrayList<TaskModel>?){
//        if (taskList != null) {
//            taskingRepository?.uploadToFirebase(taskList)
//        }
//    }

    fun retrieveTasks(){
        if (networkingRepository?.isNetworkAvailable == true) {
            taskingRepository?.startRetrievingTasks()
        }else {
            taskManagerListener?.onFailure("No Internet connection")
        }
    }

    fun getMutableTaskList() = taskingRepository?.tasksListMLD
    fun getLoggedStatus() = authenticationRepository?.getIsLoggedInMLD()
}