package com.example.movie_library.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movie_library.data.Movie
import com.example.movie_library.data.MovieDatabase
import kotlinx.coroutines.launch


@Composable
fun SearchActorScreen() {
    // Get context and coroutine scope
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val movieDao = MovieDatabase.getInstance(context).movieDao()

    // State variables for user input and results
    var query by rememberSaveable { mutableStateOf("") }
    var matchingMovies by rememberSaveable { mutableStateOf<List<Movie>>(emptyList()) }

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
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Search by Actor",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Input field for actor name
            TextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Enter actor name") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.DarkGray,
                    unfocusedContainerColor = Color.DarkGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = BrandColor,
                    unfocusedLabelColor = Color.Gray,
                    cursorColor = BrandColor
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Search button to local DB for matching actors
            Button(
                onClick = {
                    scope.launch {
                        val allMovies = movieDao.getAll()
                        matchingMovies = allMovies.filter {
                            it.actors.contains(query, ignoreCase = true)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandColor),
                elevation = ButtonDefaults.buttonElevation(6.dp)
            ) {
                Text("Search", fontSize = 16.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Show list of matching movies
            if (matchingMovies.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(matchingMovies) { movie ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Title: ${movie.title}", fontWeight = FontWeight.SemiBold, color = Color.White)
                                Text("Actors: ${movie.actors}", color = Color.LightGray)
                            }
                        }
                    }
                }
            // Show message if no results found
            } else if (query.isNotBlank()) {
                Text(
                    text = "No movies found for \"$query\"",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }
    }
}