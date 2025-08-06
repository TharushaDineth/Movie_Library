package com.example.movie_library

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movie_library.ui.AddMoviesScreen
import com.example.movie_library.ui.MainMenuScreen
import com.example.movie_library.ui.SearchActorScreen
import com.example.movie_library.ui.SearchMovieScreen
import com.example.movie_library.ui.SearchTitleScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieApp()
        }
    }
}

@Composable
fun MovieApp() {
    val navController = rememberNavController()

    MaterialTheme {
        NavHost(navController = navController, startDestination = "main_menu") {
            composable("main_menu") { MainMenuScreen(navController) }
            composable("add_movies") { AddMoviesScreen() }
            composable("search_movies") { SearchMovieScreen() }
            composable("search_actors") { SearchActorScreen() }
            composable("search_titles") { SearchTitleScreen() }
        }
    }
}