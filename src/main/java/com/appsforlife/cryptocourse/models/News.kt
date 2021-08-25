package com.appsforlife.cryptocourse.models

import com.appsforlife.cryptocourse.utils.setTime
import com.google.gson.annotations.SerializedName

data class News(
    @SerializedName("id")
    val id: String,
    @SerializedName("body")
    val body: String,
    @SerializedName("guid")
    val guid: String,
    @SerializedName("imageurl")
    val imageUrl: String,
    @SerializedName("published_on")
    val published_on: Long,
    @SerializedName("source")
    val source: String,
    @SerializedName("categories")
    val categories: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String
) {
    fun getTime(): String {
        return setTime(published_on)
    }
}