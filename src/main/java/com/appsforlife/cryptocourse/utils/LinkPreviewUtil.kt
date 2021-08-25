package com.appsforlife.cryptocourse.utils

import io.reactivex.rxjava3.core.Observable
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException


fun getJSOUPContent(url: String): Observable<Document> {
    return Observable.fromCallable {
        try {
            return@fromCallable Jsoup.connect(url).timeout(0).get()
        } catch (e: IOException) {
            return@fromCallable null
        }
    }
}