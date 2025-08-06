package com.example.movie_library.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URLEncoder
import java.net.URL

class OmdbApiClient(private val apiKey: String) {

    // searches for a movie by title using the API
    suspend fun searchMovieByTitle(title: String): Movie? {
        return withContext(Dispatchers.IO) { // Run in background thread
            try {
                // Encode the movie title
                val query = URLEncoder.encode(title, "UTF-8")

                // API request URL
                val url = URL("https://www.omdbapi.com/?t=$query&apikey=$apiKey")

                // Send request and read the response text
                val response = url.readText()

                // Convert response to JSON object
                val json = JSONObject(response)

                // If the response is successful, create and return a Movie object
                if (json.getString("Response") == "True") {
                    Movie(
                        title = json.getString("Title"),
                        year = json.getString("Year"),
                        rated = json.getString("Rated"),
                        released = json.getString("Released"),
                        runtime = json.getString("Runtime"),
                        genre = json.getString("Genre"),
                        director = json.getString("Director"),
                        writer = json.getString("Writer"),
                        actors = json.getString("Actors"),
                        plot = json.getString("Plot")
                    )
                } else null
            } catch (e: Exception) {
                null
            }
        }
    }
}