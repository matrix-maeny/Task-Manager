package com.matrix_maeny.taskmanager.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.matrix_maeny.taskmanager.R
import com.matrix_maeny.taskmanager.databinding.FragmentTaskDetailsBinding
import com.matrix_maeny.taskmanager.viewmodels.TaskViewModel

class TaskDetailsFragment : BottomSheetDialogFragment() {

    lateinit var dataBinding: FragmentTaskDetailsBinding
    lateinit var taskViewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_task_details, container, false)

        taskViewModel = arguments?.getParcelable("viewModel")!!

        dataBinding.viewModel = taskViewModel

        taskViewModel.isChecked.observe(this) {
            if (it) {
                dataBinding.resultImage.setImageResource(R.drawable.check)
            } else dataBinding.resultImage.setImageResource(R.drawable.chart)
        }

//        taskViewModel.taskingRepository?.isDeleted?.observe(this) {
//            if(it){
//                dialog?.cancel()
//                taskViewModel.taskingRepository?.isDeleted!!.postValue(false)
//            }
//        }



        return dataBinding.root
    }

}