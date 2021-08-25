package com.appsforlife.cryptocourse.models

import com.google.gson.annotations.SerializedName

data class DatumCoins(
    @SerializedName("CoinInfo")
    val coinInfo: CoinInfo
)