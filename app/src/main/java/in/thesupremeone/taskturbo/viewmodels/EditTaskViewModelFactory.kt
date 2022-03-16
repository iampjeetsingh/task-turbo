package `in`.thesupremeone.taskturbo.viewmodels

import `in`.thesupremeone.taskturbo.repository.TaskRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EditTaskViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditTaskViewModel(repository) as T
    }
}