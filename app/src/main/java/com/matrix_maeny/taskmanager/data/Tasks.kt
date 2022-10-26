package com.matrix_maeny.taskmanager.data

class Tasks() {

    var taskList:ArrayList<TaskModel>? = ArrayList()

    constructor(taskList:ArrayList<TaskModel>?):this(){
        this.taskList = taskList
    }


}