package com.appsforlife.cryptocourse.pojo

import com.google.gson.annotations.SerializedName

data class CoinInfoListOfData(
    @SerializedName("Data")
    val data: List<Datum>? = null
)