package com.appsforlife.cryptocourse.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.appsforlife.cryptocourse.api.ApiFactory
import com.appsforlife.cryptocourse.database.AppDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class NewsViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val compositeDisposable = CompositeDisposable()

    val newsList = db.newsDao().getNewsList()

    init {
        loadLastNews()
    }

    private fun loadLastNews() {
        val disposable = ApiFactory.apiService.getLastNews()
            .map { it.news }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe({
                db.newsDao().insertNews(it)
                Log.d("LOAD_NEWS", it.toString())
            }, {
                Log.d("LOAD_NEWS", "Failure ${it.message}")
            })
        compositeDisposable.addAll(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}