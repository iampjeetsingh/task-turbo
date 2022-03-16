package `in`.thesupremeone.taskturbo.repository

import `in`.thesupremeone.taskturbo.models.Task
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.*

class TaskRepository (private val database: FirebaseDatabase, private val userId: String){
    private val tasksRef: DatabaseReference = database.reference.child("Users").child(userId).child("Tasks")

    init {
        tasksRef.keepSynced(true)
    }

    private val tasksLiveData = MutableLiveData<List<Task>>()

    val tasks: LiveData<List<Task>>
        get() = tasksLiveData

    fun getTasks(){
        tasksRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val taskList = ArrayList<Task>()
                for (taskSnap in dataSnapshot.children){
                    val task = taskSnap.getValue(Task::class.java)
                    if (task != null) {
                        taskList.add(task)
                    }
                }
                tasksLiveData.postValue(taskList)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("TaskRepository", databaseError.details);
            }
        })
    }

    fun getTask(taskId: String): Task? {
        val taskSnap: DataSnapshot = Tasks.await(tasksRef.child(taskId).get())
        return taskSnap.getValue(Task::class.java)
    }

    fun updateTask(task: Task){
        tasksRef.child(task.id!!).setValue(task)
    }

    fun deleteTask(taskId: String){
        tasksRef.child(taskId).setValue(null)
    }

    fun generateTaskId(): String?{
        return tasksRef.push().key
    }
}