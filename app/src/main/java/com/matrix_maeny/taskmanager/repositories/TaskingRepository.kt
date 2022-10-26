package com.matrix_maeny.taskmanager.repositories

import android.app.Application
import android.renderscript.Sampler
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.matrix_maeny.taskmanager.R
import com.matrix_maeny.taskmanager.data.SharedData
import com.matrix_maeny.taskmanager.data.TaskModel
import com.matrix_maeny.taskmanager.data.Tasks
import com.matrix_maeny.taskmanager.utils.toast
import com.matrix_maeny.taskmanager.viewmodels.TaskViewModel

class TaskingRepository(private val application: Application) {

    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var userId: String? = FirebaseAuth.getInstance().currentUser?.uid

    var tasksListMLD: MutableLiveData<ArrayList<TaskViewModel>?>? = MutableLiveData()
    var isSaved: MutableLiveData<Boolean> = MutableLiveData()

    init {
        userId = FirebaseAuth.getInstance().currentUser?.uid
    }

    fun uploadToFirebase() {
        if (userId == null) {
            application.toast("Error -- userId")
            return
        }
//        SharedData.tasks.taskList?.add(taskModel)

        Log.i("sharedData", SharedData.tasks.taskList.toString())


        firebaseDatabase.reference.child("tasks").child(userId!!)
            .setValue(SharedData.tasks)
            .addOnCompleteListener {
                isSaved.postValue(it.isSuccessful)
                if(it.isSuccessful){
//                    application.toast("Saved successfully")
                    startRetrievingTasks()
                }else {
                    application.toast("Error: "+ (it.exception?.message ?: ""))

                }

            }.addOnFailureListener {
                application.toast("Error:"+it.message)

            }
    }

    fun updateTaskCompletion(isCompleted: Boolean, position: Int) {
//        application.toast("updating task")
        val indexPos: Int = ((SharedData.tasks.taskList?.size?.minus(1))?.minus(position) ?: -1)
        if (indexPos == -1) {
            application.toast("Can't update")
        }
        Log.i("IndexValue", "$indexPos --- $position")

        if (userId != null) {
            firebaseDatabase.reference.child("tasks").child(userId!!)
                .child("taskList")
                .child("$indexPos")
                .child("completed").setValue(isCompleted).addOnCompleteListener {

                    if (it.isSuccessful) {
                        if (isCompleted) {
                            application.toast("Completed")
                        } else
                            application.toast("Task Pending")
                    } else it.exception?.message?.let { it1 -> application.toast(it1) }
                }
        }
    }

    fun deleteTask(position: Int){
        val indexPos: Int = ((SharedData.tasks.taskList?.size?.minus(1))?.minus(position) ?: -1)
        SharedData.tasks.taskList?.removeAt(indexPos)
        uploadToFirebase()

//        val indexPos: Int = ((SharedData.tasks.taskList?.size?.minus(1))?.minus(position) ?: -1)
//        if (indexPos == -1) {
//            application.toast("Can't update")
//        }
//
//        if (userId != null) {
//            firebaseDatabase.reference.child("tasks").child(userId)
//                .child("taskList").child("$indexPos").removeValue().addOnCompleteListener{
//                    if(it.isSuccessful){
//                        application.toast("Task deleted")
//                        startRetrievingTasks()
//                    }else{
//                        it.exception?.message?.let { it1 -> application.toast(it1) }
//                    }
//                }
//        }
    }


    fun startRetrievingTasks() {
        Log.i("testingTag","start if")

        if (userId == null) return

        firebaseDatabase.reference.child("tasks").child(userId!!)//.child("taskList")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
//                        application.toast("No Tasks Yet..")
                        Log.i("testingTag","in if")
                        tasksListMLD?.postValue(null)
                        return
                    }
                    Log.i("testingTag","out if")

                    val tasks = snapshot.getValue(Tasks::class.java)
                    if (tasks != null) {
                        tasks.taskList?.let { postData(it) }
                        Log.i("testingTag","in if 2")

                    }else{
                        tasksListMLD?.postValue(null)
                        Log.i("testingTag","else if 3")

                    }
//                    postData(snapshot)
                }

                override fun onCancelled(error: DatabaseError) {
                    application.toast(error.message)
                    tasksListMLD?.postValue(null)
                }

            })

    }

    private fun postData(taskList: ArrayList<TaskModel>) {
        val exList: ArrayList<TaskViewModel> = ArrayList()
        SharedData.tasks.taskList?.clear()

        for (i in taskList) {
            val taskViewModel = TaskViewModel()
            taskViewModel.taskName = i.taskName.toString()
            taskViewModel.taskDate = i.taskDate.toString()
            taskViewModel.taskDetails = i.taskDetails.toString()
            taskViewModel.isChecked.postValue(i.isCompleted == true)

//            if (taskViewModel.isChecked)
//                taskViewModel.buttonTint = R.color.checked_button_tint
//            else taskViewModel.buttonTint = R.color.unchecked_button_tint

            exList.add(taskViewModel)

            SharedData.tasks.taskList?.add(i)

            Log.i(
                "myData",
                taskViewModel.isChecked.toString() + " --- " + taskViewModel.taskName + "-- " + taskViewModel.taskDate + "-- " + taskViewModel.taskDetails
            )
        }
        //                    application.toast("data posted")
        Log.i("myData", "posted")
        exList.reverse()
        tasksListMLD?.postValue(exList)
    }



}