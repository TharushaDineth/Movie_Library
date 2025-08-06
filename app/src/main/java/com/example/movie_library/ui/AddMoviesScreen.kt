package com.example.movie_library.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun AddMoviesScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var success by rememberSaveable { mutableStateOf(false) }

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
                text = "Add Hardcoded Movies",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Button to insert hardcoded movies into the database
            Button(
                onClick = {
                    val movieDao = MovieDatabase.getInstance(context).movieDao()
                    scope.launch(Dispatchers.IO) {
                        movieDao.insertAll(getHardcodedMovies())
                        success = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandColor),
                shape = RoundedCornerShape(10.dp),
                elevation = ButtonDefaults.buttonElevation(8.dp)
            ) {
                Text("Insert Hardcoded Movies", fontSize = 16.sp, color = Color.White)
            }

            // Show success message after adding movies
            if (success) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Movies added successfully!",
                    fontSize = 16.sp,
                    color = BrandColor
                )
            }
        }
    }
}

// Function returning a list of hardcoded Movie objects
fun getHardcodedMovies(): List<Movie> {
    return listOf(
        Movie(
            title = "The Shawshank Redemption",
            year = "1994",
            rated = "R",
            released = "14 Oct 1994",
            runtime = "142 min",
            genre = "Drama",
            director = "Frank Darabont",
            writer = "Stephen King, Frank Darabont",
            actors = "Tim Robbins, Morgan Freeman, Bob Gunton",
            plot = "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency."
        ),
        Movie(
            title = "The Godfather",
            year = "1972",
            rated = "R",
            released = "24 Mar 1972",
            runtime = "175 min",
            genre = "Crime, Drama",
            director = "Francis Ford Coppola",
            writer = "Mario Puzo, Francis Ford Coppola",
            actors = "Marlon Brando, Al Pacino, James Caan",
            plot = "An organized crime dynasty's aging patriarch transfers control of his clandestine empire to his reluctant son."
        )
    )
}