package com.appsforlife.cryptocourse.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appsforlife.cryptocourse.R
import com.appsforlife.cryptocourse.databinding.ItemCoinBinding
import com.appsforlife.cryptocourse.listeners.CoinClickListener
import com.appsforlife.cryptocourse.pojo.CoinPriceInfo
import com.bumptech.glide.Glide

class CoinInfoAdapter(private val coinClickListener: CoinClickListener) :
    RecyclerView.Adapter<CoinInfoAdapter.CoinInfoViewHolder>() {

    var coinInfoList: List<CoinPriceInfo> = listOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinInfoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCoinBinding.inflate(inflater, parent, false)
        return CoinInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoinInfoViewHolder, position: Int) {
        val coin = coinInfoList[position]
        with(holder.binding) {
            tvSymbols.text = String.format(
                tvSymbols.context.getString(R.string.symbols),
                coin.fromSymbol,
                coin.toSymbol
            )
            tvPrice.text = coin.price
            tvTime.text = coin.getTime()
            Glide.with(ivLogo.context).load(coin.getFullImageURL()).into(ivLogo)

            clMain.setOnClickListener {
                coinClickListener.onCoinClick(coin)
            }
        }
    }

    override fun getItemCount() = coinInfoList.size

    inner class CoinInfoViewHolder(internal val binding: ItemCoinBinding) :
        RecyclerView.ViewHolder(binding.root)
}