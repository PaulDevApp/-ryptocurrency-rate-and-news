package com.appsforlife.cryptocourse.models

import com.google.gson.annotations.SerializedName

data class CoinInfoListOfData(
    @SerializedName("Data")
    val datumCoins: List<DatumCoins>
)