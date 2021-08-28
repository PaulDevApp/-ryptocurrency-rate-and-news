package com.appsforlife.cryptocourse.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.appsforlife.cryptocourse.api.ApiFactory
import com.appsforlife.cryptocourse.database.AppDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class NewsViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val compositeDisposable = CompositeDisposable()

    var isLoading = MutableLiveData<Boolean>()

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
                isLoading.postValue(false)
                Log.d("LOAD_NEWS", it.toString())
            }, {
                isLoading.postValue(true)
                Log.d("LOAD_NEWS", "Failure ${it.message}")
            })
        compositeDisposable.addAll(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}