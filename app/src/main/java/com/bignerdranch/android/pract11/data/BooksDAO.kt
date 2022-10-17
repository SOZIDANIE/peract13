package com.bignerdranch.android.pract11.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bignerdranch.android.pract11.data.models.KnigaTypes
import com.bignerdranch.android.pract11.data.models.KnigaZhanr

@Dao
interface BooksDAO{

    /* Таблица Книг*/
    @Query("SELECT * FROM $BOOKS_TABLE")
    fun getAllBooks(): LiveData<MutableList<KnigaTypes>>

    @Insert
    fun addBook(knigaTypes: KnigaTypes)

    @Update
    fun saveBook(knigaTypes: KnigaTypes)

    @Delete
    fun killBook(knigaTypes: KnigaTypes)

    /* Таблица Жанров*/
    @Query("SELECT * FROM $BOOKS_ZHANR_TABLE")
    fun getAllZhanrs(): LiveData<MutableList<KnigaZhanr>>

    @Query("SELECT zhanr FROM $BOOKS_ZHANR_TABLE WHERE id=:index")
    fun getZhanr(index:Int): LiveData<String>

    @Insert
    fun addZhanr(booksZhanr: KnigaZhanr)

    @Update
    fun saveZhanr(booksZhanrSave: KnigaZhanr)

    @Delete
    fun killZhanr(booksZhanrDelete: KnigaZhanr)

}