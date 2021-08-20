package com.appsforlife.cryptocourse.utils

import androidx.recyclerview.widget.DiffUtil
import com.appsforlife.cryptocourse.pojo.CoinInfo

class ItemDiffUtil(
    private val oldList: List<CoinInfo>,
    private val newList: List<CoinInfo>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[oldItemPosition]
    }
}