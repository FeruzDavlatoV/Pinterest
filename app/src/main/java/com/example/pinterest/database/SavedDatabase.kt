package com.example.pinterest.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pinterest.database.model.Profile


@Database(entities = [Profile::class],version = 2)
abstract class SavedDatabase: RoomDatabase() {

    abstract fun savedDao():ProfileDao

    companion object{

        @Volatile
        private var INSTANCE:SavedDatabase?=null
        fun getDatabase(context: Context):SavedDatabase{
            synchronized(this){
                if (INSTANCE==null){
                    INSTANCE= Room.databaseBuilder(context,SavedDatabase::class.java,"saved.db")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE!!
        }
    }
}