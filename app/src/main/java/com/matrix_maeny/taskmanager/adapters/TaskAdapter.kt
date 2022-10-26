package com.matrix_maeny.taskmanager.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.matrix_maeny.taskmanager.R
import com.matrix_maeny.taskmanager.data.TaskModel
import com.matrix_maeny.taskmanager.databinding.TaskModelBinding
import com.matrix_maeny.taskmanager.viewmodels.TaskViewModel

class TaskAdapter(private val context: Context, private val taskList: List<TaskViewModel>) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {

        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        val taskBinding: TaskModelBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.task_model, parent, false)

        return TaskViewHolder(taskBinding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        taskList[position].itemId = position
        holder.bindViewModel(taskList[position])

    }

    override fun getItemCount(): Int  = taskList.size

    class TaskViewHolder(taskB: TaskModelBinding) : RecyclerView.ViewHolder(taskB.root) {

        private val taskBinding = taskB

       fun bindViewModel(taskViewModel: TaskViewModel){
           taskBinding.viewModel = taskViewModel
       }

        fun getTaskModelBinding():TaskModelBinding = taskBinding
    }
}