package com.bignerdranch.android.pract11

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.edit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.bignerdranch.android.pract11.data.DATABASE_NAME
import com.bignerdranch.android.pract11.data.KnigaDB
import com.bignerdranch.android.pract11.data.models.KnigaTypes
import com.bignerdranch.android.pract11.data.models.KnigaZhanr
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import java.util.concurrent.Executors

class zapomnit : AppCompatActivity() {
    private val Books: MutableList<KnigaTypes> = mutableListOf()
    private val Book: MutableList<KnigaZhanr> = mutableListOf()
    private lateinit var b1: Button
    private lateinit var db: KnigaDB
    private var index = -1
    private var count = 0
    private var count1 = 0
    private var correct = 0
    private var prov = Books.size
    private lateinit var editText : EditText
    private lateinit var editText1 :EditText
    private lateinit var editText2 : EditText
    private lateinit var editText3 : EditText
    private lateinit var bb : ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zapomnit)
        b1 = findViewById(R.id.buttons)
        editText = findViewById(R.id.editTextTextPersonName)//name
        editText1 = findViewById(R.id.editTextTextPersonName3)//author
        editText2 = findViewById(R.id.editTextNumber)//pages
        editText3 = findViewById(R.id.editTextTextPersonName2)//type
        bb = findViewById(R.id.imageButton)
        db = Room.databaseBuilder(this, KnigaDB::class.java, DATABASE_NAME).build()
        index = intent.getIntExtra("number", -1)
        upInfo()
        Log.d("123", prov.toString())
        bb.setOnClickListener {
            super.onBackPressed()
        }
    }
    private fun updateBook(){
        selectTV()
        b1.setOnClickListener {
            if(count==0)
            {
                for(i in Books){
                    if (editText.text.toString() != Books[count1].name && editText1.text.toString() != Books[count1].author && editText3.text.toString() != Book[count1].zhanr)
                    {
                        correct++
                    }
                    else
                    {
                        Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                    }
                }
                if(correct>0)
                {
                    if (index == -1) {
                        val uuidType = UUID.randomUUID()
                        val uuidZhanr = UUID.randomUUID()
                        Executors.newSingleThreadExecutor().execute {
                            db.knigaDAO().addBook(KnigaTypes(uuidType, "${editText.text}", "${editText1.text}", "${editText2.text}"))
                            db.knigaDAO().addZhanr(KnigaZhanr(uuidZhanr, uuidType, "${editText3.text}", Date()))
                            count++
                        }
                    } else if (index != -1) {
                        Log.d("INDEX", "123")
                        Executors.newSingleThreadExecutor().execute {
                            db.knigaDAO().saveBook(KnigaTypes(Books[index].idKniga, "${editText.text}", "${editText1.text}", "${editText2.text}"))
                            db.knigaDAO().saveZhanr(KnigaZhanr(Book[index].id, Books[index].idKniga, "${editText3.text}", Date()))
                            count++
                        }
                    }
                }
            }
            else
            {
                b1.isEnabled = false
            }
        }
    }
    private fun selectTV(){
        if(index > -1){
            editText.setText(Books[index].name)
            editText1.setText(Books[index].author)
            editText2.setText(Books[index].numPages)
        }
    }
    private fun upInfo(){
        Books.clear()
        Book.clear()
        db.knigaDAO().getAllZhanrs().observe(this) {
            Book.addAll(it)
            if(index > -1){
                runOnUiThread(Runnable {
                    kotlin.run {
                        editText3.setText(Book[index].zhanr)
                    }
                })
            }
        }
        db.knigaDAO().getAllBooks().observe(this, androidx.lifecycle.Observer {
            Books.addAll(it)
            updateBook()
        })
    }
}

