package `in`.thesupremeone.taskturbo.activities

import `in`.thesupremeone.taskturbo.App
import `in`.thesupremeone.taskturbo.R
import `in`.thesupremeone.taskturbo.databinding.ActivityEditTaskBinding
import `in`.thesupremeone.taskturbo.repository.TaskRepository
import `in`.thesupremeone.taskturbo.viewmodels.EditTaskViewModel
import `in`.thesupremeone.taskturbo.viewmodels.EditTaskViewModelFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class EditTaskActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditTaskBinding
    lateinit var viewModel: EditTaskViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_task)

        val app = application as App
        val user = Firebase.auth.currentUser
        val repository = TaskRepository(app.getDatabase(), user!!.uid)
        viewModel = ViewModelProvider(this, EditTaskViewModelFactory(repository))[EditTaskViewModel::class.java]

        viewModel.taskId.observe(this){
            if(it!=null){
                binding.saveButton.text = "Update"
                binding.deleteButton.visibility = View.VISIBLE
                binding.completedCheckBox.visibility = View.VISIBLE
                viewModel.getTask()
            }else{
                binding.saveButton.text = "Save"
                binding.deleteButton.visibility = View.GONE
                binding.completedCheckBox.visibility = View.GONE
            }
        }

        viewModel.task.observe(this){
            binding.titleEditText.setText(it.title)
            binding.descriptionEditText.setText(it.description)
            if(it.completed!=null){
                binding.completedCheckBox.setChecked(it.completed!!)
            }
        }

        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val description = binding.descriptionEditText.text.toString()
            val completed = binding.completedCheckBox.isChecked

            if(title.isEmpty()){
                Toast.makeText(this,"Please provide a title",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.saveTask(title, description, completed)
            Toast.makeText(this,"Task Updated!!!",Toast.LENGTH_SHORT).show()
        }

        binding.deleteButton.setOnClickListener {
            viewModel.deleteTask()
            Toast.makeText(this,"Task Deleted!!!",Toast.LENGTH_SHORT).show()
            finish()
        }

        val taskId: String? = intent.getStringExtra("taskId")
        viewModel.setTaskId(taskId)
    }
}