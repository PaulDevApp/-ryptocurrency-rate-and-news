package com.appsforlife.cryptocourse.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.appsforlife.cryptocourse.models.CoinInfo

@Dao
interface CoinDao {

    @Query("SELECT * FROM coin_info_list")
    fun getPriceList(): LiveData<List<CoinInfo>>

    @Query("SELECT * FROM coin_info_list WHERE id == :id LIMIT 1")
    fun getDetail(id: String): LiveData<CoinInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCoinList(coinList: List<CoinInfo?>?)

}