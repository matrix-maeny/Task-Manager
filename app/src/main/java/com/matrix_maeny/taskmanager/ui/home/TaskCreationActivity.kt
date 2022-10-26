package com.matrix_maeny.taskmanager.ui.home

import android.app.DatePickerDialog
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.matrix_maeny.taskmanager.R
import com.matrix_maeny.taskmanager.data.TaskProgressDialog
import com.matrix_maeny.taskmanager.databinding.ActivityTaskCreationBinding
import com.matrix_maeny.taskmanager.databinding.ActivityTaskManagerBinding
import com.matrix_maeny.taskmanager.listeners.TaskCreationListener
import com.matrix_maeny.taskmanager.repositories.NetworkingRepository
import com.matrix_maeny.taskmanager.utils.getTaskingRepository
import com.matrix_maeny.taskmanager.utils.toast
import com.matrix_maeny.taskmanager.viewmodels.TaskCreationViewModel
import java.time.Year

class TaskCreationActivity : AppCompatActivity(), TaskCreationListener {

    lateinit var dataBinding: ActivityTaskCreationBinding
    lateinit var taskCreationViewModel: TaskCreationViewModel
    lateinit var taskProgressDialog: TaskProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_creation)

        dataBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_task_creation
        )
        taskCreationViewModel = ViewModelProvider(this)[TaskCreationViewModel::class.java]
        taskCreationViewModel.taskingRepository = getTaskingRepository(application)
        taskCreationViewModel.taskCreationListener = this
        taskCreationViewModel.networkingRepository = NetworkingRepository(application)

        dataBinding.viewModel = taskCreationViewModel
        setSupportActionBar(dataBinding.toolbar)
        supportActionBar?.title = "Create Task"
        taskProgressDialog = TaskProgressDialog(this, "Creating Task", "Please wait...")



//        dataBinding.button.setOnClickListener {
//           openDatePickerDialog()
//        }

        taskCreationViewModel.getSavedStatus()?.observe(this) {
            taskProgressDialog.dismiss()
            finish()

        }

        taskCreationViewModel.networkingRepository?.observe(this){
            if(!it){
                taskProgressDialog.dismiss()
                toast("Please connect to a network")
                finish()
            }
        }




    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.task_creation_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // save the task
        taskProgressDialog.show()
        taskCreationViewModel.saveTaskToFirebase()
//        ActivityCompat.finishAffinity(this)
        return true
    }

    override fun openDatePickerDialog() {
        val calendar: Calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            this,
            { view, year, month, dayOfMonth ->
                taskCreationViewModel.taskDate = "${dayOfMonth}/${month}/${year}"
                dataBinding.date.text = taskCreationViewModel.taskDate

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONDAY),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()

    }

    override fun uploadResult(message: String) {
        taskProgressDialog.dismiss()
        if (message == "") {
            toast("Please enter valid details")

        }else{
            toast(message)
        }

    }

}