package com.matrix_maeny.taskmanager.data

 class TaskModel(){

     var taskName:String? = null
     var taskDate:String? = null
     var taskDetails:String? = null
     var isCompleted:Boolean? = null

    constructor( taskName:String?, taskDate:String?, taskDetails:String?, isCompleted:Boolean?):this(){
        this.taskName = taskName
        this.taskDate = taskDate
        this.taskDetails = taskDetails
        this.isCompleted = isCompleted

    }
}
