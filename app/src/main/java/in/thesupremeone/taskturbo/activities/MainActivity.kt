package `in`.thesupremeone.taskturbo.activities

import `in`.thesupremeone.taskturbo.App
import `in`.thesupremeone.taskturbo.R
import `in`.thesupremeone.taskturbo.adapters.TasksAdapter
import `in`.thesupremeone.taskturbo.databinding.ActivityMainBinding
import `in`.thesupremeone.taskturbo.models.Task
import `in`.thesupremeone.taskturbo.repository.TaskRepository
import `in`.thesupremeone.taskturbo.viewmodels.MainViewModel
import `in`.thesupremeone.taskturbo.viewmodels.MainViewModelFactory
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel
    private val tasksAdapter: TasksAdapter = object : TasksAdapter(){
        override fun onTaskStatusUpdate(task: Task, checked: Boolean) {
            task.completed = checked
            viewModel.updateTask(task)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val app: App = application as App
        val database: FirebaseDatabase = app.getDatabase()
        val user: FirebaseUser? = Firebase.auth.currentUser
        val repository = TaskRepository(database, user!!.uid)

        viewModel = ViewModelProvider(this, MainViewModelFactory(repository))[MainViewModel::class.java]

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(this@MainActivity, EditTaskActivity::class.java)
            startActivity(intent)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = tasksAdapter

        viewModel.tasks.observe(this) {
            tasksAdapter.setTaskList(it)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.refreshTasks()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.logout -> logout()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logout(){
        Toast.makeText(this,"Logout Successful!!!",Toast.LENGTH_SHORT).show()
        Firebase.auth.signOut();
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}