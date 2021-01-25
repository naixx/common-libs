package common.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

interface WithId {
    val id: Long
    override operator fun equals(other: Any?): Boolean
}

class DiffCallback<T : WithId?> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = oldItem?.id == newItem?.id

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
}

class SimpleDiffCallback<T> : DiffUtil.ItemCallback<T?>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
}
