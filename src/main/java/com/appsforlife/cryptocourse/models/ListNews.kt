package com.appsforlife.cryptocourse.models

import com.google.gson.annotations.SerializedName

data class ListNews(
    @SerializedName("Data")
    val news: List<News>
)