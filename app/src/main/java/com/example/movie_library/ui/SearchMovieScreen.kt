package com.example.movie_library.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
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
import com.example.movie_library.data.OmdbApiClient
import kotlinx.coroutines.launch

// Retain Movie state across screen rotations
val MovieSaver = listSaver<Movie, String>(
    save = { movie ->
        listOf(
            movie.title, movie.year, movie.rated, movie.released, movie.runtime,
            movie.genre, movie.director, movie.writer, movie.actors, movie.plot
        )
    },
    restore = {
        Movie(
            title = it[0], year = it[1], rated = it[2], released = it[3], runtime = it[4],
            genre = it[5], director = it[6], writer = it[7], actors = it[8], plot = it[9]
        )
    }
)

@Composable
fun SearchMovieScreen() {
    // Setup required variables and state
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val movieDao = MovieDatabase.getInstance(context).movieDao()
    val apiClient = remember { OmdbApiClient("f17716d1") }

    var query by rememberSaveable { mutableStateOf("") }
    var hasMovie by rememberSaveable { mutableStateOf(false) }
    var savedMovie by rememberSaveable(stateSaver = MovieSaver) {
        mutableStateOf(Movie("", "", "", "", "", "", "", "", "", ""))
    }
    var message by rememberSaveable { mutableStateOf("") }

    val movie = if (hasMovie) savedMovie else null

    // Styling
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
                text = "Search by Movie Title",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Input field for entering movie title
            TextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Enter movie title") },
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

            // Button to fetch movie details from the API
            Button(
                onClick = {
                    scope.launch {
                        val result = apiClient.searchMovieByTitle(query)
                        if (result != null) {
                            savedMovie = result
                            hasMovie = true
                            message = ""
                        } else {
                            hasMovie = false
                            message = "Movie not found!"
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
                Text("Retrieve Movie", fontSize = 16.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Show movie details if available
            if (movie != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Title: ${movie.title}", fontWeight = FontWeight.SemiBold, color = Color.White)
                        Text("Year: ${movie.year}", color = Color.LightGray)
                        Text("Rated: ${movie.rated}", color = Color.LightGray)
                        Text("Released: ${movie.released}", color = Color.LightGray)
                        Text("Runtime: ${movie.runtime}", color = Color.LightGray)
                        Text("Genre: ${movie.genre}", color = Color.LightGray)
                        Text("Director: ${movie.director}", color = Color.LightGray)
                        Text("Writer: ${movie.writer}", color = Color.LightGray)
                        Text("Actors: ${movie.actors}", color = Color.LightGray)
                        Text("Plot: ${movie.plot}", color = Color.LightGray)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        scope.launch {
                            movieDao.insertAll(listOf(movie))
                            message = "Movie saved to Database"
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandColor),
                    elevation = ButtonDefaults.buttonElevation(6.dp)
                ) {
                    Text("Save Movie to Database", fontSize = 16.sp, color = Color.White)
                }
            }

            if (message.isNotBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(message, color = BrandColor)
            }
        }
    }
}