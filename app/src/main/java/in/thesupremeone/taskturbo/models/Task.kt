package `in`.thesupremeone.taskturbo.models

data class Task(
    var id: String? = null,
    var title: String? = null,
    var description: String? = null,
    var completed: Boolean? = null
)
