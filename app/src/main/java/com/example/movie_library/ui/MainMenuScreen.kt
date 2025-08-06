package com.example.movie_library.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// Define custom colors for branding and background
val BrandColor = Color(0xFF49A2A6)
val DarkBackground = Color(0xFF121212)

// Main screen displaying menu options to navigate to different features
@Composable
fun MainMenuScreen(navController: NavController) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        color = DarkBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Movie Library",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Button to navigate to Add Movies screen
            MenuButton("Add Movies to DB") {
                navController.navigate("add_movies")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Button to navigate to Search Movies screen
            MenuButton("Search for Movies") {
                navController.navigate("search_movies")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Button to navigate to Search Actors screen
            MenuButton("Search for Actors") {
                navController.navigate("search_actors")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Button to navigate to Search Titles screen
            MenuButton("Search Movies by Title") {
                navController.navigate("search_titles")
            }
        }
    }
}

// Reusable styled button component
@Composable
fun MenuButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = BrandColor),
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = ButtonDefaults.buttonElevation(8.dp)
    ) {
        Text(text = text, fontSize = 16.sp, color = Color.White)
    }
}