package com.bignerdranch.android.pract11

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.bignerdranch.android.pract11.data.DATABASE_NAME
import com.bignerdranch.android.pract11.data.KnigaDB
import com.bignerdranch.android.pract11.data.models.KnigaTypes
import com.bignerdranch.android.pract11.data.models.KnigaZhanr
import java.util.*
import java.util.concurrent.Executors

class razbor : AppCompatActivity() {
    private val DeleteBook: MutableList<KnigaTypes> = mutableListOf()
    private val DeleteBookZhanr: MutableList<KnigaZhanr> = mutableListOf()
    private lateinit var db: KnigaDB
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_razbor)
        val b2 = findViewById<ImageButton>(R.id.i)
        val tvNAME = findViewById<TextView>(R.id.textView11)
        val tvAUTHOR = findViewById<TextView>(R.id.textView13)
        val tvPAGES = findViewById<TextView>(R.id.textView15)
        val tvZHANR = findViewById<TextView>(R.id.textView17)
        val buttonDelte = findViewById<Button>(R.id.button)
        val buttonChange = findViewById<Button>(R.id.button2)
        db = Room.databaseBuilder(this, KnigaDB::class.java, DATABASE_NAME).build()
        val knigaDAO = db.knigaDAO()
        val executor = Executors.newSingleThreadExecutor()
        val index = intent.getIntExtra("number", -1)
        val intent = Intent(this, zapomnit::class.java)
        val toast = Toast.makeText(applicationContext, "Удалили)", Toast.LENGTH_SHORT)
        upInfo()
        /*
        executor2.execute {
        //в чём твоя проблема мразь
            tvNAME.text = DeleteBook[index].name
            tvAUTHOR.text = DeleteBook[index].author
            tvPAGES.text = DeleteBook[index].numPages
            tvZHANR.text = DeleteBookZhanr[index].zhanr
        }
        */
        buttonChange.setOnClickListener{
            intent.putExtra("number", index)
            startActivity(intent)
        }
        buttonDelte.setOnClickListener {
            executor.execute{
                knigaDAO.killBook(KnigaTypes(DeleteBook[index].idKniga, DeleteBook[index].name, DeleteBook[index].author, DeleteBook[index].numPages))
                knigaDAO.killZhanr(KnigaZhanr(DeleteBookZhanr[index].id,DeleteBook[index].idKniga, DeleteBookZhanr[index].zhanr, Date()))
            }
            toast.show()
        }
        b2.setOnClickListener {
            super.onBackPressed()
        }
    }

    private fun upInfo(){
        DeleteBook.clear()
        DeleteBookZhanr.clear()
        db.knigaDAO().getAllBooks().observe(this, androidx.lifecycle.Observer {
            DeleteBook.addAll(it)
        })
        db.knigaDAO().getAllZhanrs().observe(this) {
            DeleteBookZhanr.addAll(it)
        }
    }
    override fun onResume() {
        super.onResume()
        upInfo()
    }

}

