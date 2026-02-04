package com.example.devfactory777todoapp.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.devfactory777todoapp.data.TodoRepository
import com.example.devfactory777todoapp.data.TopicRepository
import com.example.devfactory777todoapp.models.Task
import com.example.devfactory777todoapp.models.Topic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TopicDetailsViewModel(private val topicId: String) : ViewModel() {
    private val todoRepository = TodoRepository()
    private val topicRepository = TopicRepository()

    // We need to fetch the topic details (including tasks)
    // Actually, TopicRepository.getTopics() fetches all topics.
    // For a single topic details, we might want a specific query or just pass the topic object.
    // However, navigation passes ID. So we need to re-fetch or find from cached list.
    // Since we don't have a shared data source (cache), let's fetch specific topic or filter.
    // Supabase Postgrest allows filtering.
    // We didn't implement 'getTopic(id)' in repository. Let's add that or just refetch all? 
    // Refetching all is inefficient. Let's start with basic implementation.
    // Wait, the UI structure expects a 'Topic' object which contains 'tasks'.
    // If we only fetch tasks, we still need the Topic metadata (name, color, etc).
    // Let's assume for now we can fetch the fresh topic.
    // But TopicRepository doesn't have `getTopic(id)`. 
    // I made `getTopics()` return List<Topic>.
    // Let's add a `fetchTopic` to this ViewModel that effectively refreshes the view.
    // Ideally we should update TopicRepository to support 'getTopicById'.
    // Since I can't easily change multiple files at once without complex steps, 
    // I will use a simple workaround: If we can't fetch single, I'll update Repository first?
    // Actually, I can just update the Repository in the next step or assume I'll add it.
    // Let's modify TopicRepository to have `getTopic(id)` first?
    // Or I can execute a query here directly? No, repo pattern.
    // Let's try to stick to existing repo method if possible, or add one.
    // Current `TopicRepository.getTopics()` gets all. I can filter on client side for MVP.
    // It's not efficient but works for now.
    
    private val _topic = MutableStateFlow<Topic?>(null)
    val topic: StateFlow<Topic?> = _topic.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadTopic()
    }

    fun loadTopic() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Temporary inefficient approach: retrieve all and find.
                // In production, add 'getTopic(id)' to repository.
                val topics = topicRepository.getTopics()
                _topic.value = topics.find { it.id == topicId }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addTask(title: String) {
        val currentTopic = _topic.value ?: return
        if (title.isBlank()) return

        viewModelScope.launch {
            try {
                val newTask = Task(
                    id = java.util.UUID.randomUUID().toString(), // Helper to ensure ID is generated
                    title = title,
                    topicId = currentTopic.id,
                    isCompleted = false
                )
                android.util.Log.d("TopicDetailsVM", "Creating new task object: $newTask")
                todoRepository.addTask(newTask)
                android.util.Log.d("TopicDetailsVM", "Task added to repository, refreshing topic")
                loadTopic() // Refresh
            } catch (e: Exception) {
                android.util.Log.e("TopicDetailsVM", "Error adding task", e)
                e.printStackTrace()
            }
        }
    }

    fun toggleTaskCompletion(task: Task, isCompleted: Boolean) {
        viewModelScope.launch {
            try {
                todoRepository.updateTaskCompletion(task.id, isCompleted)
                // Optimistic update or refresh
                // Let's refresh for correctness
                loadTopic()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            try {
                todoRepository.deleteTask(task.id)
                loadTopic()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

class TopicDetailsViewModelFactory(private val topicId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TopicDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TopicDetailsViewModel(topicId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
