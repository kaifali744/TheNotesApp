package com.example.thenotesapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.thenotesapp.model.Note
import java.util.concurrent.locks.Lock

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase(){

    //Abstract function to get an instance of NoteDAO
    abstract fun getNoteDao(): NoteDao

    //Companion object code can be accessed from anywhere in code because it is static
    companion object{

        // Volatile indicates that other threads could immediately observe the changes made by this thread
        @Volatile
        //instance variable holds the singleton instance of NoteDatase
        private var instance : NoteDatabase? = null
        //LOCK object is used to synchronize the DataBase creation process
        //This Block ensures that only one thread can execute the code inside this block at a time
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance?:
        synchronized(LOCK){
            instance?:
            createDataBase(context).also{
                instance = it
            }
        }

        //function to create DB using Room
        private fun createDataBase(context: Context) =
            Room.databaseBuilder(
                context,
                NoteDatabase::class.java,
                "note_db"
            ).build()

    }


}