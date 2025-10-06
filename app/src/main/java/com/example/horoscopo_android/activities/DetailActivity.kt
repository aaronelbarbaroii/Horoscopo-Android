package com.example.horoscopo_android.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.horoscopo_android.R
import com.example.horoscopo_android.data.Horoscope
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.example.horoscopo_android.utils.SessionManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.progressindicator.LinearProgressIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class DetailActivity : AppCompatActivity() {

    lateinit var nameTextView: TextView
    lateinit var dateTextView: TextView
    lateinit var iconImageView: ImageView
    lateinit var horoscope: Horoscope
    lateinit var session: SessionManager
    lateinit var favoriteItem: MenuItem
    lateinit var progressIndicator: LinearProgressIndicator
    lateinit var horoscopeLuckTextView: TextView
    lateinit var periodNavigationView: BottomNavigationView

    var isFavorite = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        session = SessionManager(this)

        val id = intent.getStringExtra("HOROSCOPE_ID")!!

        isFavorite = session.isFavorite(id)


        nameTextView = findViewById(R.id.nameTextView)
        dateTextView = findViewById(R.id.dateTextView)
        iconImageView = findViewById(R.id.iconImageView)
        horoscopeLuckTextView = findViewById(R.id.horoscopeLuckTextView)
        progressIndicator = findViewById(R.id.progressIndicator)
        periodNavigationView = findViewById(R.id.periodNavigationView)
        periodNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_daily -> {
                    getHoroscopeLuck("daily")
                    true
                }
                R.id.action_weekly -> {
                    getHoroscopeLuck("weekly")
                    true
                }
                R.id.action_monthly -> {
                    getHoroscopeLuck("monthly")
                    true
                }
                else -> false
            }
        }


        horoscope = Horoscope.getById(id)

        nameTextView.setText(horoscope.name)
        dateTextView.setText(horoscope.dates)
        iconImageView.setImageResource(horoscope.icon)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(horoscope.name)
        supportActionBar?.setSubtitle(horoscope.dates)

        getHoroscopeLuck()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.activity_detail_menu, menu)

        favoriteItem = menu.findItem(R.id.action_favorite)
        setFavoriteMenu()

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_favorite -> {
                isFavorite = !isFavorite
                if (isFavorite) {
//                    session.setFavorite(horoscope.id)
                    session.setFavorite(horoscope.id, horoscope.id)
                } else {
//                    session.setFavorite("")
                    session.setFavorite("", horoscope.id)
                }
                setFavoriteMenu()
                true
            }

            R.id.action_share -> {
//                Log.i("MENU", "He pulsado el menú compartir")
                val texto = horoscopeLuckTextView.text.toString()
                shared(texto)
                true
            }

            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    fun setFavoriteMenu() {
        if (isFavorite) {
            favoriteItem.setIcon(R.drawable.ic_menu_favorite_selected)
        } else {
            favoriteItem.setIcon(R.drawable.ic_menu_favorite)
        }
    }

    private fun shared(texto: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "$texto")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    fun getHoroscopeLuck(period: String = "daily") {
        progressIndicator.show()
        horoscopeLuckTextView.setText(R.string.horoscope_delay_text)
        CoroutineScope(Dispatchers.IO).launch {
            val url = URL("https://horoscope-app-api.vercel.app/api/v1/get-horoscope/$period?sign=${horoscope.id}&day=TODAY")
            // HTTP Connexion
            val connection = url.openConnection() as HttpsURLConnection
            // Method
            connection.setRequestMethod("GET")

            try {
                // Response code
                val responseCode = connection.getResponseCode()

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Read the response
                    val response = readStream(connection.inputStream)

                    val jsonResponse = JSONObject(response)
                    val result = jsonResponse.getJSONObject("data").getString("horoscope_data")

                    CoroutineScope(Dispatchers.Main).launch {
                        horoscopeLuckTextView.text = result
                        progressIndicator.hide()
                    }

                    Log.i("API", result)
                } else {
                    Log.e("API", "Server response: $responseCode")
                }
            } catch (e: Exception) {
                Log.e("API", "Error", e)
            } finally {
                connection.disconnect()
            }
        }
    }

    fun readStream (input: InputStream) : String {
        val reader = BufferedReader(InputStreamReader(input))
        val response = StringBuffer()
        var inputLine: String? = null

        while ((reader.readLine().also { inputLine = it }) != null) {
            response.append(inputLine)
        }
        reader.close()
        return response.toString()
    }


}
