package com.example.devfactory777todoapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devfactory777todoapp.ui.theme.Primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTopicScreen(
    onBackClick: () -> Unit,
    onCreateTopic: () -> Unit
) {
    var topicName by remember { mutableStateOf("") }
    var selectedIcon by remember { mutableStateOf("star") }
    var selectedColor by remember { mutableStateOf(Primary) }

    val icons = listOf("home", "work", "book", "fitness_center", "favorite", "shopping_cart", "flight", "palette")
    val colors = listOf(
        Primary, Color(0xFFF43F5E), Color(0xFF10B981), Color(0xFFF59E0B),
        Color(0xFF6366F1), Color(0xFFA855F7), Color(0xFFF97316), Color(0xFF1E293B)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "New Topic",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onBackClick) {
                        Text("Cancel", color = Primary, fontSize = 16.sp)
                    }
                },
                actions = {
                    TextButton(onClick = onCreateTopic) {
                        Text("Create", color = Primary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Topic Name Section
            SectionHeader("Topic Name")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8FAFC), RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(12.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(selectedColor, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(getIconForName(selectedIcon), contentDescription = null, tint = Color.White)
                }
                TextField(
                    value = topicName,
                    onValueChange = { topicName = it },
                    placeholder = { Text("e.g., Personal Finances", color = Color.LightGray) },
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                )
            }

            // Choose Icon Section
            SectionHeader("Choose Icon")
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.height(160.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(icons) { iconName ->
                    val isSelected = selectedIcon == iconName
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) Primary else Color(0xFFF8FAFC))
                            .border(1.dp, if (isSelected) Primary else Color(0xFFF1F5F9), RoundedCornerShape(12.dp))
                            .clickable { selectedIcon = iconName },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            getIconForName(iconName),
                            contentDescription = null,
                            tint = if (isSelected) Color.White else Color.Gray
                        )
                    }
                }
            }

            // Pick Color Section
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                SectionHeader("Pick Color")
                Text("Blue", color = Primary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                colors.forEach { color ->
                    val isSelected = selectedColor == color
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(color)
                            .let {
                                if (isSelected) it.border(2.dp, Color.White, CircleShape).padding(2.dp).border(2.dp, Primary, CircleShape) else it
                            }
                            .clickable { selectedColor = color }
                    )
                }
            }

            // Hint Text
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Primary.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                    .border(1.dp, Primary.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = "Topics help you group your tasks by area of life. You can add unique colors and icons to find them faster.",
                    fontSize = 14.sp,
                    color = Color(0xFF4C779A),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onCreateTopic,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                Text("Create Topic", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            TextButton(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Dismiss", color = Color.Gray, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun SectionHeader(text: String) {
    Text(
        text = text.uppercase(),
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = Color.LightGray,
        letterSpacing = 1.sp
    )
}
