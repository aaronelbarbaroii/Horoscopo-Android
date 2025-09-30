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
    lateinit var adapter: HorosocopeAdapter

    var horoscopeList: List<Horoscope> = Horoscope.getAll()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.setTitle(R.string.activity_name_title)

        recyclerView = findViewById(R.id.recyclerView)

        adapter = HorosocopeAdapter(horoscopeList, { position ->
            val horoscope = horoscopeList[position]
            goToDetail(this, horoscope)
        })

        recyclerView.adapter = adapter

        recyclerView.layoutManager = LinearLayoutManager(this)
    }


    override fun onResume() {
        super.onResume()

        adapter.notifyDataSetChanged()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.acdtivity_main_menu, menu)

        val searchView = menu!!.findItem(R.id.action_search).actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                horoscopeList = Horoscope.getAll().filter {
                    getString(it.name).contains(newText, true)
                            || getString(it.dates).contains(newText, true)
                }

                adapter.updateItems(horoscopeList)
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