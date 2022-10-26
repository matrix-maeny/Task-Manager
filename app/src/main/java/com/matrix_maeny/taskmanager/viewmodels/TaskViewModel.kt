package com.matrix_maeny.taskmanager.viewmodels

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import android.widget.CompoundButton
import androidx.databinding.Bindable
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.matrix_maeny.taskmanager.R
import com.matrix_maeny.taskmanager.data.SharedData
import com.matrix_maeny.taskmanager.fragments.TaskDetailsFragment
import com.matrix_maeny.taskmanager.listeners.TaskManagerListener
import com.matrix_maeny.taskmanager.repositories.NetworkingRepository
import com.matrix_maeny.taskmanager.repositories.TaskingRepository

class TaskViewModel() : ViewModel(), Parcelable {

    var taskName: String = "Task Name"
    var taskDate: String = "Task date"
    var taskDetails: String = ""
    val isChecked: MutableLiveData<Boolean> = MutableLiveData()
    var itemId: Int = 0

    var taskingRepository: TaskingRepository? = null
    var taskManagerListener: TaskManagerListener? = null

    var networkingRepository: NetworkingRepository? = null


    constructor(parcel: Parcel) : this() {
        taskName = parcel.readString().toString()
        taskDate = parcel.readString().toString()
        taskDetails = parcel.readString().toString()
        itemId = parcel.readInt()
    }


    fun onCheckedChange(compoundButton: CompoundButton, isChecked: Boolean) {
        if (networkingRepository?.isNetworkAvailable == true) {
            this.isChecked.postValue(isChecked)

            taskManagerListener?.checkedChange(compoundButton)
            SharedData.tasks.taskList?.get(itemId)?.isCompleted = isChecked
            taskingRepository?.updateTaskCompletion(isChecked, itemId)
        } else {
            taskManagerListener?.onFailure("Please connect to a network")
            compoundButton.isChecked = false
        }

        // you need to upload to firebase database
//        if (taskList != null) {
//            taskingRepository?.uploadToFirebase(taskList)
//        }


    }


    fun onCardClick(view: View) {

        taskManagerListener?.showDetailsFragment(this)
    }

    fun onDeleteClick(view: View) {
        if (networkingRepository?.isNetworkAvailable == true) {
            taskingRepository?.deleteTask(itemId)
            taskManagerListener?.cancelBottomDialog()
        } else
            taskManagerListener?.onFailure("Please connect to a network")

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(taskName)
        parcel.writeString(taskDate)
        parcel.writeString(taskDetails)
        parcel.writeInt(itemId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TaskViewModel> {
        override fun createFromParcel(parcel: Parcel): TaskViewModel {
            return TaskViewModel(parcel)
        }

        override fun newArray(size: Int): Array<TaskViewModel?> {
            return arrayOfNulls(size)
        }
    }


}