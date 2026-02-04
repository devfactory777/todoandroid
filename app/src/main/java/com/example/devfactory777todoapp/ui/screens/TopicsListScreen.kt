package com.example.devfactory777todoapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devfactory777todoapp.models.Task
import com.example.devfactory777todoapp.models.Topic
import com.example.devfactory777todoapp.ui.theme.*

import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicsListScreen(
    onTopicClick: (String) -> Unit,
    onAddTopicClick: () -> Unit,
    viewModel: TopicsListViewModel = viewModel()
) {
    val topics by viewModel.topics.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()


    // Refresh topics when screen is potentially revisited
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadTopics()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTopicClick,
                containerColor = Primary,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Topic")
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Header()
            SearchBar()
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Primary)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(topics) { topic ->
                        TopicCard(topic = topic, onClick = { onTopicClick(topic.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = { },
            modifier = Modifier
                .size(40.dp)
                .background(Color.LightGray.copy(alpha = 0.2f), CircleShape)
        ) {
            Icon(Icons.Default.AccountCircle, contentDescription = "Account")
        }
    }
    Text(
        text = "Topics",
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar() {
    var text by remember { mutableStateOf("") }
    TextField(
        value = text,
        onValueChange = { text = it },
        placeholder = { Text("Search topics...", color = Color.Gray) },
        leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null, tint = Color.Gray) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            cursorColor = Primary
        )
    )
}

@Composable
fun TopicCard(topic: Topic, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(topic.colorHex), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = getIconForName(topic.iconName),
                        contentDescription = null,
                        tint = getIconTintForName(topic.iconName)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = topic.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    val statusText = if (topic.completionPercentage == 100) "Done" else "${topic.completionPercentage}% complete"
                    Text(
                        text = "${topic.totalCount} Tasks • $statusText",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = Color.LightGray)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                LinearProgressIndicator(
                    progress = topic.completionPercentage / 100f,
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = if (topic.completionPercentage == 100) Green600 else Primary,
                    trackColor = Color(0xFFF1F5F9)
                )
                if (topic.completionPercentage == 100) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Green600, modifier = Modifier.size(16.dp))
                } else {
                    Text(
                        text = "${topic.completionPercentage}%",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (topic.completionPercentage > 0) Primary else Color.LightGray
                    )
                }
            }
        }
    }
}

fun getIconForName(name: String): ImageVector {
    return when (name) {
        "work" -> Icons.Default.Work
        "home" -> Icons.Default.Home
        "shopping_cart" -> Icons.Default.ShoppingCart
        "fitness_center" -> Icons.Default.FitnessCenter
        "star" -> Icons.Default.Star
        "book" -> Icons.Default.Book
        "favorite" -> Icons.Default.Favorite
        "flight" -> Icons.Default.Flight
        "palette" -> Icons.Default.Palette
        else -> Icons.Default.List
    }
}

fun getIconTintForName(name: String): Color {
    return when (name) {
        "work" -> Blue600
        "home" -> Purple600
        "shopping_cart" -> Green600
        "fitness_center" -> Orange600
        else -> Primary
    }
}
