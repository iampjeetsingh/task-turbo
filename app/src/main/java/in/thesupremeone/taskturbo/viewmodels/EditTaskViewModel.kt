package `in`.thesupremeone.taskturbo.viewmodels

import `in`.thesupremeone.taskturbo.models.Task
import `in`.thesupremeone.taskturbo.repository.TaskRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditTaskViewModel(private val repository: TaskRepository) : ViewModel() {

    private val taskIdMutable = MutableLiveData<String?>()

    val taskId: LiveData<String?>
    get() = taskIdMutable

    fun setTaskId(taskId: String?){
        taskIdMutable.postValue(taskId)
    }


    private val taskMutable = MutableLiveData<Task>()

    val task: LiveData<Task>
    get() = taskMutable


    fun saveTask(title: String, description: String, completed: Boolean){
        var id = taskId.value
        if(id==null){
            id = repository.generateTaskId()
            taskIdMutable.postValue(id)
        }
        val task = Task(id, title, description, completed)
        repository.updateTask(task)
    }

    fun deleteTask(){
        val id = taskId.value
        if(id!=null){
            repository.deleteTask(id)
        }
    }

    fun getTask(){
        val id = taskId.value
        if(id!=null){
            viewModelScope.launch(Dispatchers.IO) {
                val task = repository.getTask(id)
                withContext(Dispatchers.Main){
                    taskMutable.postValue(task!!)
                }
            }
        }
    }
}