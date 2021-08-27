package com.appsforlife.cryptocourse.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.appsforlife.cryptocourse.models.CoinInfo
import com.appsforlife.cryptocourse.models.News

@Database(entities = [CoinInfo::class, News::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        private var db: AppDatabase? = null
        private const val DB_NAME = "name"
        private val LOCK = Any()

        fun getInstance(context: Context): AppDatabase {
            synchronized(LOCK) {
                db?.let { return it }
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    DB_NAME
                ).allowMainThreadQueries().build()
                db = instance
                return instance
            }
        }
    }

    abstract fun coinPriceInfoDao(): CoinDao
    abstract fun newsDao(): NewsDao
}