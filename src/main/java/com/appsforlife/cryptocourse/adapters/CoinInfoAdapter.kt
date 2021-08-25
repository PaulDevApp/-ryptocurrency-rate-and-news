package com.appsforlife.cryptocourse.adapters

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.appsforlife.cryptocourse.databinding.ItemCoinBinding
import com.appsforlife.cryptocourse.listeners.CoinClickListener
import com.appsforlife.cryptocourse.models.CoinInfo
import com.appsforlife.cryptocourse.utils.CoinDiffUtil
import com.appsforlife.cryptocourse.utils.setAnim
import com.bumptech.glide.Glide

class CoinInfoAdapter(private val coinClickListener: CoinClickListener) :
    RecyclerView.Adapter<CoinInfoAdapter.CoinInfoViewHolder>() {

    private var oldList = emptyList<CoinInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinInfoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCoinBinding.inflate(inflater, parent, false)
        return CoinInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoinInfoViewHolder, position: Int) {
        val coin = oldList[position]
        with(holder.binding) {
            Glide.with(ivItemLogo.context).load(coin.getFullImageURL()).into(ivItemLogo)

            ivItemLogo.drawable?.let {
                val bitmap = (it as BitmapDrawable).bitmap
                createPalette(bitmap, holder)
            }

            tvItemName.text = coin.name
            tvItemLaunchDesc.text = coin.assetLaunchDate
            tvItemDocumentDesc.text = coin.documentType
            tvItemProofDesc.text = coin.proofType
            tvItemAlgorithmDesc.text = coin.algorithm

            setAnim(cardView, 350)


            clItem.setOnClickListener {
                coinClickListener.onCoinClick(coin.id, clItem)
            }

        }
    }

    private fun createPalette(bitmap: Bitmap, holder: CoinInfoViewHolder) {
        Palette.from(bitmap).generate { palette ->
            val swatch = palette?.vibrantSwatch
            if (swatch != null) {
                holder.binding.tvItemName.setTextColor(swatch.rgb)
            }
        }
    }

    fun setData(newList: List<CoinInfo>) {
        val diffUtil = CoinDiffUtil(oldList, newList)
        val result = DiffUtil.calculateDiff(diffUtil)
        oldList = newList
        result.dispatchUpdatesTo(this)
    }


    override fun getItemCount() = oldList.size

    inner class CoinInfoViewHolder(internal val binding: ItemCoinBinding) :
        RecyclerView.ViewHolder(binding.root)
}