package com.example.devfactory777todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.devfactory777todoapp.ui.screens.AddTopicScreen
import com.example.devfactory777todoapp.ui.screens.TopicDetailsScreen
import com.example.devfactory777todoapp.ui.screens.TopicsListScreen
import com.example.devfactory777todoapp.ui.theme.TodoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TodoApp()
                }
            }
        }
    }
}

@Composable
fun TodoApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "topics_list") {
        composable("topics_list") {
            TopicsListScreen(
                onTopicClick = { topicId ->
                    navController.navigate("topic_details/$topicId")
                },
                onAddTopicClick = {
                    navController.navigate("add_topic")
                }
            )
        }
        composable("topic_details/{topicId}") { backStackEntry ->
            val topicId = backStackEntry.arguments?.getString("topicId") ?: ""
            TopicDetailsScreen(
                topicId = topicId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable("add_topic") {
            AddTopicScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onCreateTopic = {
                    navController.popBackStack()
                }
            )
        }
    }
}
