package com.matrix_maeny.taskmanager.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.matrix_maeny.taskmanager.R
import com.matrix_maeny.taskmanager.adapters.TaskAdapter
import com.matrix_maeny.taskmanager.data.TaskProgressDialog
import com.matrix_maeny.taskmanager.databinding.ActivityTaskManagerBinding
import com.matrix_maeny.taskmanager.fragments.TaskDetailsFragment
import com.matrix_maeny.taskmanager.listeners.TaskManagerListener
import com.matrix_maeny.taskmanager.repositories.NetworkingRepository
import com.matrix_maeny.taskmanager.ui.auth.LoginActivity
import com.matrix_maeny.taskmanager.utils.getAuthenticationRepository
import com.matrix_maeny.taskmanager.utils.getTaskingRepository
import com.matrix_maeny.taskmanager.utils.toast
import com.matrix_maeny.taskmanager.viewmodels.TaskManagerViewModel
import com.matrix_maeny.taskmanager.viewmodels.TaskViewModel

class TaskManagerActivity : AppCompatActivity(), TaskManagerListener {

    lateinit var dataBinding: ActivityTaskManagerBinding
    lateinit var taskManagerViewModel: TaskManagerViewModel
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var taskAdapter: TaskAdapter
    lateinit var taskProgressDialog: TaskProgressDialog

    //    private var taskList = ArrayList<TaskModel>()
    private var taskViewModelList = ArrayList<TaskViewModel>()
    private val taskDetailsFragment = TaskDetailsFragment()


    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_manager)


//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//        )

        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_task_manager)
        setSupportActionBar(dataBinding.toolbar)

        taskManagerViewModel = ViewModelProvider(this)[TaskManagerViewModel::class.java]
        taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        taskViewModel.taskManagerListener = this


        taskManagerViewModel.authenticationRepository = getAuthenticationRepository(application)
        val taskingRepository = getTaskingRepository(application)

        taskViewModel.taskingRepository = taskingRepository
        taskManagerViewModel.taskingRepository = taskingRepository
        taskManagerViewModel.taskManagerListener = this
        taskManagerViewModel.networkingRepository = NetworkingRepository(application)


//        val taskViewModel2: TaskViewModel = TaskViewModel()
//        taskViewModel2.taskName = "Hello world"
//        taskViewModel2.taskDate = "12/12/12"
//        taskViewModel2.taskDetails = "fffffff"

        taskProgressDialog = TaskProgressDialog(this, "Retrieving Data", "Please wait...")
        taskProgressDialog.show()


        taskAdapter = TaskAdapter(this, taskViewModelList)
        dataBinding.recyclerView.adapter = taskAdapter
        dataBinding.recyclerView.layoutManager = LinearLayoutManager(this)

        taskManagerViewModel.getMutableTaskList()?.observe(this) {

            dataBinding.swipeRefreshLayout.isRefreshing = false
            taskViewModelList.clear()
            if (it != null) {

                for (i in it) {
                    i.taskManagerListener = this
                    i.taskingRepository = taskingRepository
                    i.networkingRepository = taskManagerViewModel.networkingRepository

                    taskViewModelList.add(i)


                }
            }

            taskAdapter.notifyDataSetChanged()

            dataBinding.tvEmpty.text = "No Tasks Yet"
            dataBinding.tvEmpty.visibility =
                if (taskViewModelList.isEmpty()) {
                    toast("No Tasks Yet..")
                    View.VISIBLE
                } else View.GONE

            Log.i("testingTag","dismiss 1")

            taskProgressDialog.dismiss()

//            toast("data retrieved")
        }
        dataBinding.swipeRefreshLayout.setOnRefreshListener {
//            taskProgressDialog.show()
            taskManagerViewModel.retrieveTasks()


        }

        taskManagerViewModel.networkingRepository?.observe(this) {

            dataBinding.tvEmpty.text = "Please connect to a network"


            if (taskViewModelList.isEmpty() && !it) {
                dataBinding.tvEmpty.visibility = View.VISIBLE
                toast("Please connect to a network")
            } else {
                dataBinding.tvEmpty.visibility = View.GONE
                if (!it)
                    toast("Please connect to a network")
            }


        }

//        taskManagerViewModel.getLoggedStatus()?.observe(this){
//            taskProgressDialog.dismiss()
//        }

        taskManagerViewModel.retrieveTasks()


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.toolbar_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.add_task -> {
                // add task
                if (taskManagerViewModel.networkingRepository?.isNetworkAvailable == true)
                    startActivity(
                        Intent(
                            this@TaskManagerActivity,
                            TaskCreationActivity::class.java
                        )
                    )
                else onFailure("Please connect to a network")
            }
            R.id.log_out -> {
                // logout
                if (taskManagerViewModel.networkingRepository?.isNetworkAvailable == true) {
                    taskProgressDialog.setMessages("Logging out", "Please wait")
                    taskProgressDialog.show()
                    Handler().postDelayed({
                        taskProgressDialog.dismiss()
                        taskManagerViewModel.logout()
                        startActivity(Intent(this@TaskManagerActivity, LoginActivity::class.java))
                        ActivityCompat.finishAffinity(this)
                        taskViewModelList.clear()
                    }, 2500)
                } else onFailure("Please connect to a network")

            }
        }

        return true
    }

    override fun checkedChange(compoundButton: CompoundButton) {
        if (compoundButton.isChecked) {
//            toast("Checked")
            compoundButton.buttonTintList = getColorStateList(R.color.checked_button_tint)
        } else {
//            toast("Un checked")
            compoundButton.buttonTintList = getColorStateList(R.color.unchecked_button_tint)

        }

    }

    override fun showDetailsFragment(detailsViewModel: TaskViewModel) {

        val bundle = Bundle()
        bundle.putParcelable("viewModel", detailsViewModel)
        taskDetailsFragment.arguments = bundle
        taskDetailsFragment.show(supportFragmentManager, "Details Dialog")
    }

    override fun cancelBottomDialog() {
        taskDetailsFragment.dialog?.cancel()
    }

    override fun onFailure(message: String) {
        Log.i("testingTag","dismiss 3")
        taskProgressDialog.dismiss()
        dataBinding.swipeRefreshLayout.isRefreshing = false
        if (taskViewModelList.isEmpty())
            dataBinding.tvEmpty.visibility = View.VISIBLE
        toast(message)
    }


}