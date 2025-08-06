package com.example.movie_library.ui

import android.util.Log
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.net.URLEncoder

data class OmdbShortMovie(val title: String, val year: String)

// Main composable function for the title search screen
@Composable
fun SearchTitleScreen() {
    val scope = rememberCoroutineScope()
    val apiKey = "f17716d1"

    // Stores user input
    var query by rememberSaveable { mutableStateOf("") }
    // Holds list of search results
    var results by rememberSaveable { mutableStateOf<List<OmdbShortMovie>>(emptyList()) }
    // Message to display
    var message by rememberSaveable { mutableStateOf("") }

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
                text = "Search Movie Titles",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Input field for entering movie title keywords
            TextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Enter title keyword") },
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

            // Button to trigger search request to API
            Button(
                onClick = {
                    scope.launch {
                        try {
                            Log.d("OMDB_SEARCH", "Starting search for: $query")
                            val encoded = URLEncoder.encode(query, "UTF-8")
                            val url = URL("https://www.omdbapi.com/?s=$encoded&apikey=$apiKey")
                            Log.d("OMDB_SEARCH", "Request URL: $url")
                            val response = withContext(Dispatchers.IO) { url.readText() }

                            val json = JSONObject(response)
                            if (json.getString("Response") == "True") {
                                val arr = json.getJSONArray("Search")
                                val list = mutableListOf<OmdbShortMovie>()
                                for (i in 0 until minOf(10, arr.length())) {
                                    val obj = arr.getJSONObject(i)
                                    list.add(
                                        OmdbShortMovie(
                                            title = obj.getString("Title"),
                                            year = obj.getString("Year")
                                        )
                                    )
                                }
                                // Update the UI with the search results
                                results = list
                                message = ""
                            } else {
                                Log.d("OMDB_SEARCH", "OMDb response: ${json.getString("Error")}")
                                results = emptyList()
                                message = "No results found."
                            }
                        } catch (e: Exception) {
                            Log.e("OMDB_SEARCH", "Exception: ${e.message}", e)
                            results = emptyList()
                            message = "Error fetching results."
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
                Text("Search Titles", fontSize = 16.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Display movie results
            if (results.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(results) { movie ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Title: ${movie.title}", fontWeight = FontWeight.SemiBold, color = Color.White)
                                Text("Year: ${movie.year}", color = Color.LightGray)
                            }
                        }
                    }
                }
            } else if (message.isNotBlank()) {
                Text(
                    text = message,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }
    }
}