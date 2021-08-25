package com.appsforlife.cryptocourse.adapters

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.appsforlife.cryptocourse.databinding.ItemNewsBinding
import com.appsforlife.cryptocourse.models.News
import com.appsforlife.cryptocourse.utils.NewsDiffUtil
import com.appsforlife.cryptocourse.utils.setAnim
import com.bumptech.glide.Glide

class CoinNewsAdapter : RecyclerView.Adapter<CoinNewsAdapter.CoinNewsViewHolder>() {

    private var oldList = emptyList<News>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinNewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNewsBinding.inflate(inflater, parent, false)
        return CoinNewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoinNewsViewHolder, position: Int) {
        val news = oldList[position]
        with(holder.binding) {
            Glide.with(ivItemImage.context).load(news.imageUrl).into(ivItemImage)

            ivItemImage.drawable?.let {
                val bitmap = (it as BitmapDrawable).bitmap
                createPalette(bitmap, holder)
            }

            tvItemTitle.text = news.title
            tvItemCategoriesDesc.text = news.categories
            tvItemSourceDesc.text = news.source
            tvItemUrl.text = news.url
            tvItemBody.text = news.body
            tvPublished.text = news.getTime()

            setAnim(cardView, 350)

            cardView.setOnClickListener {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(news.url)
                cardView.context.startActivity(i)
            }

            if (TextUtils.isEmpty(tvItemBody.text)){
                tvItemBody.visibility = View.GONE
            }
        }
    }

    private fun createPalette(bitmap: Bitmap, holder:CoinNewsViewHolder) {
        Palette.from(bitmap).generate { palette ->
            val swatch = palette?.vibrantSwatch
            if (swatch != null) {
                holder.binding.tvItemTitle.setTextColor(swatch.rgb)
            }
        }
    }

    fun setData(newList: List<News>) {
        val diffUtil = NewsDiffUtil(oldList, newList)
        val result = DiffUtil.calculateDiff(diffUtil)
        oldList = newList
        result.dispatchUpdatesTo(this)
    }

    override fun getItemCount() = oldList.size

    inner class CoinNewsViewHolder(internal val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root)
}