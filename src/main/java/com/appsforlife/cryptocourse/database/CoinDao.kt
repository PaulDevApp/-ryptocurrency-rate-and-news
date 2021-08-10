package com.appsforlife.cryptocourse.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.appsforlife.cryptocourse.pojo.CoinPriceInfo

@Dao
interface CoinDao {

    @Query("SELECT * FROM coin_info_list ORDER BY lastUpdate DESC")
    fun getPriceList(): LiveData<List<CoinPriceInfo>>

    @Query("SELECT * FROM coin_info_list WHERE fromSymbol == :fSym LIMIT 1")
    fun getDetail(fSym:String):LiveData<CoinPriceInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCoinList(coinList:List<CoinPriceInfo>)
}