package `in`.thesupremeone.taskturbo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import `in`.thesupremeone.taskturbo.R
import `in`.thesupremeone.taskturbo.activities.EditTaskActivity
import `in`.thesupremeone.taskturbo.databinding.RowTaskBinding
import `in`.thesupremeone.taskturbo.models.Task
import android.content.Intent
import android.widget.CompoundButton

abstract class TasksAdapter : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {
    private var taskList: List<Task>? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): TaskViewHolder {
        val taskListItemBinding = DataBindingUtil.inflate<RowTaskBinding>(
            LayoutInflater.from(viewGroup.context),
            R.layout.row_task, viewGroup, false
        )
        return TaskViewHolder(taskListItemBinding)
    }

    override fun onBindViewHolder(taskViewHolder: TaskViewHolder, i: Int) {
        val currentTask = taskList!![i]
        taskViewHolder.rowTaskBinding.task = currentTask
        taskViewHolder.itemView.setOnClickListener {
            val context = taskViewHolder.itemView.context
            val intent = Intent(context, EditTaskActivity::class.java)
            intent.putExtra("taskId", currentTask.id)
            context.startActivity(intent)
        }
        taskViewHolder.rowTaskBinding.checkBox.setOnCheckedChangeListener(object: CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                onTaskStatusUpdate(currentTask, p1)
            }
        })
    }

    abstract fun onTaskStatusUpdate(task:Task, checked: Boolean);

    override fun getItemCount(): Int {
        return if (taskList != null) {
            taskList!!.size
        } else {
            0
        }
    }

    fun setTaskList(taskList: List<Task>) {
        this.taskList = taskList
        notifyDataSetChanged()
    }

    inner class TaskViewHolder(var rowTaskBinding: RowTaskBinding) :
        RecyclerView.ViewHolder(rowTaskBinding.root)
}