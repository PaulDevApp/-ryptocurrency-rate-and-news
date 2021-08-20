package com.appsforlife.cryptocourse.pojo

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class CoinPriceInfoDisplayData (
    @SerializedName("DISPLAY")
    val coinPriceInfoJsonObject: JsonObject? = null
)