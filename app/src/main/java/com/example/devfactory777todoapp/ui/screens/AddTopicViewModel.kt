package com.example.devfactory777todoapp.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devfactory777todoapp.data.TopicRepository
import com.example.devfactory777todoapp.models.Topic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddTopicViewModel : ViewModel() {
    private val repository = TopicRepository()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun createTopic(name: String, iconName: String, colorHex: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // ... (code omitted, keeping existing logic but adding logs) ...
                val colorString = Integer.toHexString(colorHex.toInt()).uppercase()
                android.util.Log.d("AddTopicVM", "Converted color: $colorString")
                
                val newTopic = Topic(
                     name = name,
                     iconName = iconName,
                     colorString = colorString
                )
                android.util.Log.d("AddTopicVM", "Calling repository createTopic")
                repository.createTopic(newTopic)
                android.util.Log.d("AddTopicVM", "Topic created, calling onSuccess")
                onSuccess()
            } catch (e: Exception) {
                android.util.Log.e("AddTopicVM", "Exception in createTopic", e)
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
