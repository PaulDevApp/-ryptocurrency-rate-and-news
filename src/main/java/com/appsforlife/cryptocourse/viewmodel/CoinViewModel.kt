package com.appsforlife.cryptocourse.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.appsforlife.cryptocourse.api.ApiFactory
import com.appsforlife.cryptocourse.database.AppDatabase
import com.appsforlife.cryptocourse.pojo.CoinInfo
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class CoinViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val compositeDisposable = CompositeDisposable()

    val priceList = db.coinPriceInfoDao().getPriceList()

    fun getDetailInfo(id: String): LiveData<CoinInfo> {
        return db.coinPriceInfoDao().getDetail(id)
    }

    init {
        loadData()
    }

    private fun loadData() {
        val disposable = ApiFactory.apiService.getTopCoins()
            .map { it -> it.data?.map { it.coinInfo } }
            .subscribeOn(Schedulers.io())
            .subscribe({
                db.coinPriceInfoDao().insertCoinList(it)
                Log.d("LOAD_DATA", "Success $it")
            }, {
                Log.d("LOAD_DATA", "Failure ${it.message}")
            })
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}