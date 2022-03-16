package `in`.thesupremeone.taskturbo.viewmodels

import `in`.thesupremeone.taskturbo.models.Task
import `in`.thesupremeone.taskturbo.repository.TaskRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel (private val repository: TaskRepository) : ViewModel(){

    init {
        refreshTasks()
    }

    val tasks : LiveData<List<Task>>
        get() = repository.tasks

    fun refreshTasks(){
        viewModelScope.launch(Dispatchers.IO){
            repository.getTasks()
        }
    }

    fun updateTask(task: Task){
        repository.updateTask(task)
    }
}