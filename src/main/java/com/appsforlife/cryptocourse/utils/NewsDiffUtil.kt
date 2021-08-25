package com.appsforlife.cryptocourse.utils

import androidx.recyclerview.widget.DiffUtil
import com.appsforlife.cryptocourse.models.News

class NewsDiffUtil(
    private val oldList: List<News>,
    private val newList: List<News>
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