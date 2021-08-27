package com.appsforlife.cryptocourse.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.appsforlife.cryptocourse.models.News

@Dao
interface NewsDao {

    @Query("SELECT * FROM news_list")
    fun getNewsList(): LiveData<List<News>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNews(listNews: List<News>)
}