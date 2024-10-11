package com.example.minigamecollection

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


// Retrofit API Interface
interface ApiService {
    @FormUrlEncoded
    @POST("db_minigame.php") // Adjust this URL based on where your PHP file is hosted
    fun saveUsername(
        @Field("username") username: String
    ): Call<ResponseBody> // Change to ResponseBody
}

class UserActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        usernameEditText = findViewById(R.id.usernameEditText)
        saveButton = findViewById(R.id.saveButton)

        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2/") // Emulator localhost for XAMPP, change to your IP address if on a real device
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        // Set button click listener
        saveButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()

            if (username.isEmpty()) {
                Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show()
            } else {
                saveUsername(username)
                val intent: Intent = Intent(
                    this@UserActivity,
                    GameList::class.java
                )
                intent.putExtra("USERNAME", username)
                startActivity(intent)
            }
        }
    }

    private fun saveUsername(username: String) {
        apiService.saveUsername(username).enqueue(object : Callback<ResponseBody> { // Use ResponseBody
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        // Convert response body to string
                        val responseBody = response.body()?.string() // Now correctly uses ResponseBody
                        val jsonResponse = JSONObject(responseBody)

                        val status = jsonResponse.getString("status")
                        val message = jsonResponse.getString("message")

                        if (status == "success") {
                            Toast.makeText(this@UserActivity, message, Toast.LENGTH_SHORT).show()
                        } else if (status == "error") {
                            Toast.makeText(this@UserActivity, message, Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(this@UserActivity, "Error parsing response", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@UserActivity, "Failed to save username", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@UserActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}