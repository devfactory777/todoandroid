package com.example.devfactory777todoapp.data

import com.example.devfactory777todoapp.data.SupabaseModule.client
import com.example.devfactory777todoapp.models.Topic
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class TopicRepository {
    private val topicTable = client.from("topics")

    suspend fun getTopics(): List<Topic> {
        // Fetch topics and their todo_items to calculate progress on the client side if needed
        return try {
            topicTable.select(columns = Columns.list("*, todo_items(*)")).decodeList<Topic>()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun createTopic(topic: Topic) {
        val userId = client.auth.currentUserOrNull()?.id
        if (userId == null) {
            android.util.Log.e("TopicRepository", "User not logged in. Cannot create topic.")
            return
        }
        val topicToCreate = topic.copy(userId = userId)
        android.util.Log.d("TopicRepository", "Creating topic: $topicToCreate")

        // Manually build JSON to exclude 'todo_items' (read-only relation) and ensure correct column names
        val json = buildJsonObject {
            put("id", topicToCreate.id)
            put("user_id", topicToCreate.userId)
            put("name", topicToCreate.name)
            put("icon_name", topicToCreate.iconName)
            put("color_hex", topicToCreate.colorString)
        }

        try {
            topicTable.insert(json)
            android.util.Log.d("TopicRepository", "Topic created successfully")
        } catch (e: Exception) {
            android.util.Log.e("TopicRepository", "Error creating topic", e)
            e.printStackTrace()
            throw e
        }
    }

    suspend fun deleteTopic(topicId: String) {
        topicTable.delete {
            filter {
                eq("id", topicId)
            }
        }
    }
}
