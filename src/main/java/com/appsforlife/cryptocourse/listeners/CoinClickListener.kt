package com.appsforlife.cryptocourse.listeners

import com.appsforlife.cryptocourse.pojo.CoinPriceInfo

interface CoinClickListener {
    fun onCoinClick(coinPriceInfo: CoinPriceInfo)
}