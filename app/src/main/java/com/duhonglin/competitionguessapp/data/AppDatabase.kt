package com.duhonglin.competitionguessapp.data

import android.content.Context
import androidx.room.Database;
import androidx.room.Room
import androidx.room.RoomDatabase
import com.duhonglin.competitionguessapp.data.models.Team

@Database(entities =[Team::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun teamDao(): TeamDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase?=null

        fun getInstance(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this){
                val instance=Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE =instance
                return instance
            }
        }
    }
}

