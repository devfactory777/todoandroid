package com.example.devfactory777todoapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devfactory777todoapp.models.Task
import com.example.devfactory777todoapp.models.Topic
import com.example.devfactory777todoapp.ui.theme.Primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicDetailsScreen(
    topicId: String,
    onBackClick: () -> Unit
) {
    // Sample data for "Weekly Groceries"
    var topic by remember {
        mutableStateOf(
            Topic(
                id = "3",
                name = "Groceries",
                iconName = "shopping_cart",
                colorHex = 0xFFDCFCE7,
                description = "Organize your shopping list",
                tasks = listOf(
                    Task("1", "Organic Honey (200g)", true),
                    Task("2", "Unsweetened Almond Milk", false),
                    Task("3", "Fresh Spinach (Bulk pack)", false),
                    Task("4", "Whole Grain Bread", false),
                    Task("5", "Greek Yogurt (Plain)", false)
                )
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Weekly Groceries",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Rounded.ArrowBackIosNew, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Rounded.MoreHoriz, contentDescription = "More")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            BottomBar()
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TopicHeader(topic)
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(topic.tasks) { task ->
                    TaskItem(
                        task = task,
                        onCheckedChange = { checked ->
                            topic = topic.copy(tasks = topic.tasks.map {
                                if (it.id == task.id) it.copy(isCompleted = checked) else it
                            })
                        },
                        onDeleteClick = {
                            topic = topic.copy(tasks = topic.tasks.filter { it.id != task.id })
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TopicHeader(topic: Topic) {
    Column(modifier = Modifier.padding(24.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column {
                Text(text = topic.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(text = topic.description, fontSize = 14.sp, color = Color.Gray)
            }
            Text(
                text = "${topic.completedCount}/${topic.totalCount} completed",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Primary
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        LinearProgressIndicator(
            progress = topic.completionPercentage / 100f,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape),
            color = Primary,
            trackColor = Color(0xFFF1F5F9)
        )
    }
}

@Composable
fun TaskItem(
    task: Task,
    onCheckedChange: (Boolean) -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = Primary,
                uncheckedColor = Color.LightGray
            ),
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = task.title,
            modifier = Modifier.weight(1f),
            fontSize = 16.sp,
            color = if (task.isCompleted) Color.LightGray else Color(0xFF0D151B),
            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
        )
        IconButton(onClick = onDeleteClick) {
            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.LightGray)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBar() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White.copy(alpha = 0.8f),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            var text by remember { mutableStateOf("") }
            TextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("Add a new item...", color = Color.Gray) },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF1F5F9),
                    unfocusedContainerColor = Color(0xFFF1F5F9),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            IconButton(
                onClick = { },
                modifier = Modifier
                    .size(56.dp)
                    .background(Primary, RoundedCornerShape(16.dp))
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task", tint = Color.White)
            }
        }
    }
}
