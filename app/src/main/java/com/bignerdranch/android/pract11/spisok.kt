package com.bignerdranch.android.pract11

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.bignerdranch.android.pract11.data.DATABASE_NAME
import com.bignerdranch.android.pract11.data.KnigaDB
import com.bignerdranch.android.pract11.data.models.KnigaTypes
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.concurrent.Executors

class spisok : AppCompatActivity() {
    private val Books: MutableList<KnigaTypes> = mutableListOf()
    private lateinit var rv: RecyclerView
    private lateinit var db: KnigaDB
    private lateinit var aV: KnigaRVAdapter
    var index = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spisok)
        rv = findViewById(R.id.recycler)
        val bb2 = findViewById<ImageButton>(R.id.i2)
        db = Room.databaseBuilder(this, KnigaDB::class.java, DATABASE_NAME).build()
        upInfo()
        bb2.setOnClickListener {
            super.onBackPressed()
        }
    }
    override fun onResume() {
        super.onResume()
        if (index != -1) {
            upInfo()
        }
    }
    private fun upInfo() {
        Books.clear()
        db.knigaDAO().getAllBooks().observe(this, androidx.lifecycle.Observer {
            Books.addAll(it)
            if (Books.isNotEmpty()) getInfo()
        })
    }
    private fun getInfo() {
        aV = KnigaRVAdapter(this, Books)
        val rvListner = object : KnigaRVAdapter.ItemClickListener {
            override fun onItemClick(view: View?, position: Int) {
                val intent = Intent(this@spisok, razbor::class.java)
                intent.putExtra("number", position)
                index = position
                startActivity(intent)
            }
        }
        aV.setClickListener(rvListner)
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv.adapter = aV
    }
}