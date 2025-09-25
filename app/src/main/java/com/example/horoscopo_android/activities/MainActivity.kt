package com.example.horoscopo_android.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.horoscopo_android.data.Horoscope
import com.example.horoscopo_android.adapters.HorosocopeAdapter
import com.example.horoscopo_android.R

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
            val horoscopo = horoscopeList[position]
            goToDatail(horoscopo)
        })

        recyclerView.adapter = adapter

        recyclerView.layoutManager = LinearLayoutManager(this)

    }

    fun goToDatail(horoscope: Horoscope){
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("HOROSCOPE_ID", horoscope.id)
        startActivity(intent)
    }

}