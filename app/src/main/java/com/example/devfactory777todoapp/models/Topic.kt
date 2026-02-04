package com.example.devfactory777todoapp.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Task(
    val id: String = java.util.UUID.randomUUID().toString(),
    @SerialName("topic_id") val topicId: String = "",
    @SerialName("content") val title: String,
    @SerialName("is_completed") val isCompleted: Boolean = false,
    @SerialName("created_at") val createdAt: String? = null
)

@Serializable
data class Topic(
    val id: String = java.util.UUID.randomUUID().toString(),
    @SerialName("user_id") val userId: String = "",
    val name: String,
    @SerialName("icon_name") val iconName: String,
    @SerialName("color_hex") val colorString: String, // Stored as "FFDBEAFE"
    @SerialName("todo_items") val tasks: List<Task> = emptyList(),
    @Transient val description: String = "" // Not present in DB
) {
    val completedCount: Int
        get() = tasks.count { it.isCompleted }

    val totalCount: Int
        get() = tasks.size

    val completionPercentage: Int
        get() = if (totalCount == 0) 0 else (completedCount * 100) / totalCount
    
    // Helper to get Color Long from String
    val colorHex: Long 
        get() = try {
            if (colorString.startsWith("0x")) {
                colorString.substring(2).toLong(16)
            } else {
                colorString.toLong(16)
            }
        } catch (e: Exception) {
            0xFFDBEAFE // Fallback color
        }
}
