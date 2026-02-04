package com.example.devfactory777todoapp.data

import com.example.devfactory777todoapp.data.SupabaseModule.client
import com.example.devfactory777todoapp.models.Task
import io.github.jan.supabase.postgrest.from

import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class TodoRepository {
    private val todoTable = client.from("todo_items")

    suspend fun addTask(task: Task) {
        val json = buildJsonObject {
            put("id", task.id)
            put("topic_id", task.topicId)
            put("content", task.title)
            put("is_completed", task.isCompleted)
        }
        try {
            android.util.Log.d("TodoRepository", "Adding task: $json")
            todoTable.insert(json)
            android.util.Log.d("TodoRepository", "Task added successfully")
        } catch (e: Exception) {
            android.util.Log.e("TodoRepository", "Error adding task", e)
            throw e
        }
    }

    suspend fun updateTaskCompletion(taskId: String, isCompleted: Boolean) {
        todoTable.update({
            set("is_completed", isCompleted)
        }) {
            filter {
                eq("id", taskId)
            }
        }
    }

    suspend fun deleteTask(taskId: String) {
        todoTable.delete {
            filter {
                eq("id", taskId)
            }
        }
    }
}
