package com.example.devfactory777todoapp.models

data class Task(
    val id: String,
    val title: String,
    val isCompleted: Boolean = false
)

data class Topic(
    val id: String,
    val name: String,
    val iconName: String,
    val colorHex: Long,
    val tasks: List<Task> = emptyList(),
    val description: String = ""
) {
    val completedCount: Int
        get() = tasks.count { it.isCompleted }

    val totalCount: Int
        get() = tasks.size

    val completionPercentage: Int
        get() = if (totalCount == 0) 0 else (completedCount * 100) / totalCount
}
