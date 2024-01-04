package com.app.apkkeuangan.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.apkkeuangan.dao.LaporanDao
import com.app.apkkeuangan.dao.UserDao
import com.app.apkkeuangan.entity.Laporan
import com.app.apkkeuangan.entity.User

@Database(
    entities = [User::class, Laporan::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase(){
    abstract fun userDao(): UserDao
    abstract fun laporanDao(): LaporanDao
    companion object{
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase{
            if(instance==null){
                instance = Room.databaseBuilder(context, AppDatabase::class.java, "DatabaseLaporan")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }
}