package com.matrix_maeny.taskmanager.listeners

import android.widget.CompoundButton
import com.matrix_maeny.taskmanager.viewmodels.TaskViewModel

interface TaskManagerListener {
    fun checkedChange(compoundButton: CompoundButton)

    fun showDetailsFragment(detailsViewModel: TaskViewModel)

    fun cancelBottomDialog()

    fun onFailure(message:String)
//    fun refreshData()

}