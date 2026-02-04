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
import com.example.devfactory777todoapp.data.SupabaseModule
import com.example.devfactory777todoapp.ui.screens.AddTopicScreen
import com.example.devfactory777todoapp.ui.screens.LoginScreen
import com.example.devfactory777todoapp.ui.screens.TopicDetailsScreen
import com.example.devfactory777todoapp.ui.screens.TopicsListScreen
import com.example.devfactory777todoapp.ui.theme.TodoAppTheme
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.handleDeeplinks

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        SupabaseModule.client.handleDeeplinks(intent)
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
    val startDestination =
        if (SupabaseModule.client.auth.currentSessionOrNull() != null) "topics_list" else "login"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("topics_list") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
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
