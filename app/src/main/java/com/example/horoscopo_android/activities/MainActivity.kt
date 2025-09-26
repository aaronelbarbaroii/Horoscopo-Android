package com.example.horoscopo_android.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.horoscopo_android.data.Horoscope
import com.example.horoscopo_android.adapters.HorosocopeAdapter
import com.example.horoscopo_android.R
import android.util.Log
import androidx.appcompat.widget.SearchView

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView

    val horoscopeList: List<Horoscope> = Horoscope.Companion.getAll()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerView)

        val adapter = HorosocopeAdapter(horoscopeList, { position ->
            val horoscope = horoscopeList[position]
            goToDetail(this, horoscope)
        })

        recyclerView.adapter = adapter

        recyclerView.layoutManager = LinearLayoutManager(this)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.acdtivity_main_menu, menu)

        val searchView = menu!!.findItem(R.id.action_search).actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.i("SEARCH", "onQueryTextSubmit: $query")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // TODO : Filtrar la lista de horóscopos y mostrarlos
                return true
            }

        })

        return true
    }





    fun goToDetail(mainActivity: MainActivity, horoscope: Horoscope) {
        val intent = Intent(mainActivity, DetailActivity::class.java)
        intent.putExtra("HOROSCOPE_ID", horoscope.id)
        mainActivity.startActivity(intent)
    }

}